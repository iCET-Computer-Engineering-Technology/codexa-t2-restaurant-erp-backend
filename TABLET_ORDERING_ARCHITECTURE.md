# Tablet Ordering System - Real-Time POS & KDS Integration

## 📋 Architecture & Plan

### System Flow Overview

```
Tablet Order Submission
    ↓
OrderController.createTabletOrder()
    ↓
OrderServiceImpl.createTabletOrder()
    ├─ Idempotency Check (idempotency_key lookup)
    ├─ Validate Order Payload
    ├─ Create Order (table_order, source=TABLET)
    ├─ Create OrderItems
    ├─ Create KDS Order (kds_orders)
    ├─ Create KDS OrderItems (kds_order_items)
    ├─ Check Menu Availability
    └─ Broadcast via WebSocket
        ├─ /topic/pos-updates → POS Screen
        ├─ /topic/kds-updates → Kitchen Display
        └─ /topic/tablet-response → Tablet (Order Confirmation)
```

### Real-Time Synchronization
- **WebSocket (STOMP)**: Used for real-time order push to POS and KDS
- **Topic `/topic/pos-updates`**: POS system receives new orders
- **Topic `/topic/kds-updates`**: Kitchen Display System receives items
- **Topic `/topic/tablet-response`**: Tablet receives confirmation
- **SLA**: 5-10 seconds from submission to POS visibility

### Database Flow
1. **orders** - Main order record with `order_type=table_order`, `order_source=TABLET`
2. **order_items** - Individual line items
3. **kds_orders** - Kitchen Display System order wrapper
4. **kds_order_items** - KDS item tracking with status (pending→in_progress→done)

### Idempotency Strategy
- **Field**: `orders.idempotency_key` (unique, not null for tablet orders)
- **Logic**: Check if `idempotency_key` exists in DB before insert
- **Return**: If exists, return existing order instead of creating duplicate
- **Tablet Retry**: Can safely retry POST without double-order risk

---

## 📊 Database Schema

### `orders` Table
```sql
CREATE TABLE orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_type_id INT,
  order_number VARCHAR(50) UNIQUE,
  order_type ENUM('dine_in','takeout','booking','table_order') NOT NULL,
  table_id INT,
  customer_id INT,
  server_id INT,
  status ENUM('open','sent_to_kitchen','partially_ready','ready','paid','voided'),
  subtotal DECIMAL(10,2),
  discount_amount DECIMAL(10,2),
  tax_amount DECIMAL(10,2),
  service_charge DECIMAL(10,2),
  total_amount DECIMAL(10,2),
  notes TEXT,
  order_source VARCHAR(50) DEFAULT 'STAFF',           -- TABLET or STAFF
  idempotency_key VARCHAR(100) UNIQUE,                -- UUID for dedup
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (table_id) REFERENCES tables(id),
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (server_id) REFERENCES users(id),
  UNIQUE KEY unique_idempotency (idempotency_key)
);
```

### `order_items` Table
```sql
CREATE TABLE order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  menu_item_id INT NOT NULL,
  portion_id INT NOT NULL,
  quantity INT DEFAULT 1,
  price DECIMAL(10,2),
  status ENUM('pending','fired','ready','served','voided') DEFAULT 'pending',
  notes TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
  FOREIGN KEY (portion_id) REFERENCES portions(id)
);
```

### `kds_orders` Table
```sql
CREATE TABLE kds_orders (
  id INT PRIMARY KEY AUTO_INCREMENT,
  order_id INT NOT NULL,
  displayed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  bumped_at DATETIME,
  bumped_by INT,
  is_rush TINYINT DEFAULT 0,
  is_vip TINYINT DEFAULT 0,
  color_status ENUM('green','yellow','red') DEFAULT 'green',
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (bumped_by) REFERENCES users(id)
);
```

### `kds_order_items` Table
```sql
CREATE TABLE kds_order_items (
  id INT PRIMARY KEY AUTO_INCREMENT,
  kds_order_id INT NOT NULL,
  order_item_id INT NOT NULL,
  status ENUM('pending','in_progress','done') DEFAULT 'pending',
  fired_at DATETIME,
  completed_at DATETIME,
  FOREIGN KEY (kds_order_id) REFERENCES kds_orders(id) ON DELETE CASCADE,
  FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE
);
```

---

## 🔧 Backend Implementation

### Entities

#### Order Entity
- Already has: `id`, `orderTypeId`, `orderNumber`, `orderType`, `tableId`, `customerId`, `serverId`, `status`
- **Add fields**: `source` (TABLET/STAFF), `idempotencyKey`

#### OrderItem Entity
- Already complete: `id`, `orderId`, `menuItemId`, `portionId`, `quantity`, `price`, `status`, `notes`

#### KdsOrder Entity (New)
```java
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class KdsOrder {
    private Integer id;
    private Integer orderId;
    private LocalDateTime displayedAt;
    private LocalDateTime bumpedAt;
    private Integer bumpedBy;
    private Boolean isRush;
    private Boolean isVip;
    private String colorStatus;
}
```

#### KdsOrderItem Entity (New)
```java
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class KdsOrderItem {
    private Integer id;
    private Integer kdsOrderId;
    private Integer orderItemId;
    private String status; // pending, in_progress, done
    private LocalDateTime firedAt;
    private LocalDateTime completedAt;
}
```

---

## 🚀 API Endpoints

### POST `/api/order/create-tablet`
**Purpose**: Tablet places order at table

**Request** (with idempotency):
```json
{
  "tableId": 5,
  "customerId": 12,
  "serverId": 3,
  "orderType": "table_order",
  "source": "TABLET",
  "idempotencyKey": "uuid-550e8400-e29b-41d4-a716-446655440000",
  "notes": "No onions",
  "items": [
    {
      "menuItemId": 10,
      "portionId": 2,
      "quantity": 2,
      "price": 1200.0,
      "notes": "extra spicy"
    }
  ]
}
```

**Response 201 (Success)**:
```json
{
  "success": true,
  "message": "Order placed successfully",
  "order": {
    "id": 123,
    "orderNumber": "ORD-20260401-0042",
    "orderType": "table_order",
    "tableId": 5,
    "status": "open",
    "source": "TABLET",
    "totalAmount": 2400.0,
    "createdAt": "2026-04-01T08:30:45",
    "items": [...]
  },
  "kdsOrderId": 456
}
```

**Response 400 (Duplicate)**:
```json
{
  "success": false,
  "message": "Order already exists with this idempotency key",
  "existingOrderId": 123
}
```

**Response 400 (Validation Error)**:
```json
{
  "success": false,
  "message": "Item unavailable",
  "details": {
    "menuItemId": 10,
    "reason": "OUT_OF_STOCK"
  }
}
```

### GET `/api/order/find-by-id/{id}`
**Purpose**: POS fetches order details by ID

### WebSocket Topics

#### `/topic/pos-updates` (Broadcast)
Sent whenever a new tablet order is placed:
```json
{
  "event": "NEW_TABLET_ORDER",
  "order": { ...full OrderResponse... },
  "timestamp": "2026-04-01T08:30:45"
}
```

#### `/topic/kds-updates` (Broadcast)
Sent whenever a new KDS order is created:
```json
{
  "event": "NEW_KDS_ORDER",
  "kdsOrder": {
    "id": 456,
    "orderId": 123,
    "items": [
      { "id": 789, "menuItemId": 10, "status": "pending", ... }
    ]
  },
  "timestamp": "2026-04-01T08:30:45"
}
```

#### `/topic/tablet-response` (Unicast to specific tablet)
Sent to confirm order received:
```json
{
  "status": "SUCCESS",
  "orderId": 123,
  "orderNumber": "ORD-20260401-0042",
  "message": "Order placed successfully"
}
```

---

## 📝 Implementation Code

### 1. Update Order Entity
- Add `source` and `idempotencyKey` fields

### 2. DTOs for Tablet Order
- `TabletOrderRequest` (with idempotencyKey, source, etc.)
- `TabletOrderResponse` (success message + order + kdsOrderId)

### 3. KDS Repository & Service
- Create KDS entities, repositories, services
- Methods: `saveKdsOrder()`, `saveKdsOrderItem()`, `updateKdsItemStatus()`

### 4. OrderService Enhancement
- Add `createTabletOrder()` with idempotency + KDS creation
- Validate availability before commit
- Broadcast to websocket topics

### 5. OrderController Enhancement
- Add `POST /api/order/create-tablet` endpoint

### 6. WebSocket Broadcast Logic
- Inject `SimpMessagingTemplate` into OrderService
- Send events to `/topic/pos-updates` and `/topic/kds-updates`

---

## ✅ Acceptance Criteria Map

| Criterion | Implementation |
|-----------|-----------------|
| Real-time sync (5-10s) | WebSocket STOMP broadcast on order creation |
| Order tagged "Table Order" | `orderType = table_order` set in request |
| Table number + source | `tableId` and `order_source = TABLET` validated |
| Kitchen visibility | KDS record + items created; broadcast to `/topic/kds-updates` |
| Tablet success message | Response includes success status + order summary |
| Idempotency (no duplicates) | `idempotency_key` checked before insert; return existing if found |

---

## 🧪 Testing Flow

1. **Happy Path**: 
   - POST tablet order with unique idempotency key
   - Verify order created, KDS created, websocket broadcasts sent
   - Check POS receives update within 2-5 seconds

2. **Duplicate Submission**:
   - POST same order twice with same idempotency key
   - Verify second returns 400 with existing order ID
   - Verify no duplicate order_items

3. **Out-of-Stock Item**:
   - POST order with unavailable item
   - Verify 400 response with stock reason
   - Verify no order created

4. **KDS Workflow**:
   - Create tablet order
   - Verify KDS has pending items
   - Update KDS item status to in_progress, then done
   - Verify broadcasts sent to kitchen display


