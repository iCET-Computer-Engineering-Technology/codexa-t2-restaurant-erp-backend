# Restaurant ERP Frontend Full Documentation (Copilot Ready)

## 1) Purpose
This document is a complete frontend implementation guide for the current backend API.
It includes:
- Recommended frontend architecture
- Page/module breakdown
- Auth and role handling
- HTTP and WebSocket integration
- Full endpoint catalog with request and response contracts

Use this file as the single source when generating frontend code with Copilot.

---

## 2) Backend Contract Snapshot
- API style: Spring REST + DTOs + JWT auth
- WebSocket: STOMP over SockJS
- Auth type: Bearer JWT in `Authorization` header
- CORS: localhost allowed (`http://localhost:*`, `http://127.0.0.1:*`)

### Base URL
Set from environment:
- `VITE_API_BASE_URL` (Vite) or `NEXT_PUBLIC_API_BASE_URL` (Next.js)
- Example local: `http://localhost:8080`

### Standard Headers
- `Content-Type: application/json`
- `Authorization: Bearer <token>` (except public endpoints)

---

## 3) Roles and Access Rules
From `SecurityConfig`:

### Public
- `POST /api/auth/login`
- `POST /api/auth/register`
- Swagger/docs/error endpoints

### Admin only (`ROLE_ADMIN`)
- `/api/admin/**`
- `/admin/**`
- `/ingredient/**`
- `/api/campaigns/**`
- `/api/campaign-analytics/**`
- `/api/emails/**`
- `/api/categories/**`
- `/api/menu-items/**` (write endpoints still admin)
- `/api/portions/**` (write endpoints still admin)
- `/api/menu-item-price/**` (write endpoints still admin)

### Staff and Admin (`ROLE_ADMIN`, `ROLE_USER`, `ROLE_CASHIER`, `ROLE_WAITER`, `ROLE_CHEF`)
- `/api/order/**`
- `/api/kitchen/**`
- `/customers/**`
- `/api/waiter/**`
- `/api/supplier/**`

### Read-only shared for staff
`GET` only:
- `/api/menu-items/**`
- `/api/menu-item-price/**`
- `/api/portions/**`
- `/tables/**`, `/api/tables/**`

---

## 4) Suggested Frontend Stack and Structure
## 4.1 Recommended stack
- React + TypeScript
- React Router
- TanStack Query (or RTK Query)
- Axios (or Fetch wrapper)
- Zustand or Redux Toolkit for auth/session
- SockJS + STOMP client for realtime

## 4.2 Suggested folder layout
```txt
src/
  app/
    router.tsx
    providers.tsx
  api/
    client.ts
    auth.api.ts
    orders.api.ts
    kitchen.api.ts
    reservations.api.ts
    customers.api.ts
    menu.api.ts
    supplier.api.ts
    campaigns.api.ts
    analytics.api.ts
    email.api.ts
  features/
    auth/
    pos/
    kitchen/
    reservations/
    crm/
    menu/
    campaigns/
    admin/
  websocket/
    stomp.ts
    topics.ts
  types/
    dto.ts
    errors.ts
  utils/
    auth-storage.ts
    role-guards.ts
```

## 4.3 Route map
- `/login`
- `/dashboard`
- `/pos/orders`
- `/kitchen/board`
- `/reservations`
- `/customers`
- `/menu`
- `/campaigns`
- `/admin/automation`
- `/admin/email-scheduler`

Use route guards by role.

---

## 5) DTO Contracts (Frontend Types)
These are the key request/response bodies used by frontend.

## 5.1 Auth
```ts
type RegisterRequestDto = {
  username: string;
  email: string;
  password: string;
  role?: "ROLE_ADMIN" | "ROLE_USER" | "ROLE_CASHIER" | "ROLE_WAITER" | "ROLE_CHEF";
  time?: string; // LocalTime
};

type LoginRequestDto = {
  username: string;
  password: string;
};

type AuthResponse = {
  token: string;
  tokenType: "Bearer";
  username: string;
  role: string;
};
```

## 5.2 Orders
```ts
type OrderItemCreateRequest = {
  menuItemId: number;
  portionId: number;
  quantity: number; // >= 1
  price: number; // > 0
  notes?: string;
};

type OrderCreateRequest = {
  orderTypeId?: number;
  orderType: string;
  tableId?: number;
  customerId?: number;
  serverId?: number;
  notes?: string;
  items: OrderItemCreateRequest[];
};

type TabletOrderRequest = {
  orderType: string; // table_order
  tableId?: number;
  customerId?: number;
  serverId?: number;
  notes?: string;
  idempotencyKey: string;
  items: OrderItemCreateRequest[];
};

type OrderStatusUpdateRequest = {
  status: string;
};

type OrderItemResponse = {
  id: number;
  orderId: number;
  menuItemId: number;
  portionId: number;
  quantity: number;
  price: number;
  lineTotal: number;
  status: string;
  notes?: string;
  createdAt: string;
};

type OrderResponse = {
  id: number;
  orderTypeId?: number;
  orderNumber: string;
  orderType: string;
  tableId?: number;
  customerId?: number;
  serverId?: number;
  status: string;
  subTotal: number;
  discountAmount: number;
  taxAmount: number;
  serviceCharge: number;
  totalAmount: number;
  notes?: string;
  source?: string;
  createdAt: string;
  updatedAt: string;
  items: OrderItemResponse[];
};

type TabletOrderResponse = {
  success: boolean;
  message: string;
  order?: OrderResponse;
  kdsOrderId?: number;
  details?: Record<string, unknown>;
};
```

## 5.3 Reservations
```ts
type BookingRequestDto = {
  reservationDate: string; // YYYY-MM-DD
  reservationTime: string; // HH:mm:ss
  partySize: number; // 1..20
  customerName: string;
  email?: string;
  phone: string; // 10 digits
  tableId?: number;
  notes?: string;
};

type AvailableSlotDto = {
  time: string;
  availableTables: number;
  isAvailable: boolean;
};

type ReservationDto = {
  id: number;
  customerId: number;
  customerName?: string;
  email?: string;
  phone?: string;
  tableId: number;
  tableNumber?: string;
  partySize: number;
  reservationDate: string;
  reservationTime: string;
  status?: string;
  confirmationCode?: string;
  notes?: string;
};
```

## 5.4 Customer/Menu/Other
```ts
type CustomerDto = {
  id?: number;
  firstName: string;
  lastName: string;
  email?: string;
  phone: string;
  address?: string;
  preferredLanguage?: string;
  dietaryNotes?: string;
  communicationEmail?: number;
  communicationSms?: number;
  gdprDeleted?: number;
  birthday?: string;
  loyaltyPoints?: number;
  createdAt?: string;
};

type TableDto = { id: number; tableNumber: string; capacity: number; status: string };
type SupplierDto = { id?: number; name: string; contactName?: string; email?: string; phone?: string; address?: string };
type MenuCategoriesDto = { id?: number; name: string; isActive?: boolean };
type MenuItemsDto = { id?: number; categoryId?: number; categoryName?: string; name?: string; description?: string; isAvailable?: boolean; imageUrl?: string; createdAt?: string; updatedAt?: string };
type PortionsDto = { id?: number; name: string };
type MenuItemPriceDto = { id?: number; itemId: number; portionId: number; price: number; isActive: boolean; itemName?: string; categoryName?: string; portionName?: string };

type UpdateOrderStatus = { orderId: number; waiterId: number; status: string };
type AssignWaiterRequest = { kitchenOrderId: number; waiterId: number };

type MarketingCampaignDto = {
  id?: number;
  campaignName: string;
  segmentId?: number;
  channel: "email" | "sms";
  subject?: string;
  bodyTemplate?: string;
  abTestEnabled?: boolean;
  variantBBody?: string;
  scheduledAt?: string;
  sentAt?: string;
  status?: "draft" | "scheduled" | "sent" | "cancelled";
  createdBy?: number;
  createdAt?: string;
};

type MarketingEmailRequest = {
  recipientEmail?: string;
  recipientName?: string;
  templateType?: "promotional" | "birthday" | "anniversary" | "campaign";
  templateVariables?: Record<string, string>;
  subject?: string;
  expiryDate?: string;
};

type EmailSchedulerConfigDto = { id?: number; sendTime: string };
```

## 5.5 Common error response
```ts
type ErrorResponse = {
  status: number;
  message: string;
  error: string;
  timestamp: string;
  path: string;
  details?: Record<string, string>;
};
```

---

## 6) Full API Catalog (Requests and Responses)
All paths below are relative to `API_BASE_URL`.

## 6.1 Auth (`/api/auth`)
1. `POST /api/auth/register`
   - Auth: Public
   - Request body: `RegisterRequestDto`
   - Success: `201` + `AuthResponse`
   - Error: `400/401` + `ErrorResponse`

2. `POST /api/auth/login`
   - Auth: Public
   - Request body: `LoginRequestDto`
   - Success: `200` + `AuthResponse`
   - Error: `401` + `ErrorResponse`

## 6.2 Orders (`/api/order`)
1. `POST /api/order/create`
   - Request: `OrderCreateRequest`
   - Response: `201` + `OrderResponse`

2. `POST /api/order/create-tablet`
   - Request: `TabletOrderRequest`
   - Response: `201` or `400` + `TabletOrderResponse`

3. `GET /api/order/find-all`
   - Response: `200` + `OrderResponse[]`

4. `GET /api/order/find-open-orders`
   - Response: `200` + `OrderResponse[]`

5. `GET /api/order/find-by-status/{status}`
   - Response: `200` + `OrderResponse[]`

6. `PUT /api/order/update/{orderId}/status`
   - Request: `OrderStatusUpdateRequest`
   - Response: `200` + string message

7. `PUT /api/order/{id}/type?type={value}`
   - Request: query param `type`
   - Response: `200` + string message

8. `GET /api/order/find-by-id/{id}`
   - Response: `200` + `OrderResponse`

9. `GET /api/order/find-all-with-item-names`
   - Response: `200` + `OrderWithItemNameResponse[]`

10. `GET /api/order/find-with-item-names/{id}`
   - Response: `200` + `OrderWithItemNameResponse`

## 6.3 Customers (`/customers`)
1. `GET /customers`
   - Response: `CustomerDto[]`

2. `POST /customers`
   - Request: `CustomerDto`
   - Response: `boolean`

3. `GET /customers/phone/{phone}`
   - Response: `CustomerDto`

4. `GET /customers/{id}`
   - Response: `CustomerDto`

5. `DELETE /customers/{phone}`
   - Response: `boolean`

6. `PUT /customers`
   - Request: `CustomerDto`
   - Response: `boolean`

7. `GET /customers/{id}/profile`
   - Response: `CustomerProfileDto`

## 6.4 Reservations (`/reservations` and `/api/reservations`)
Both base paths are valid.

1. `GET /reservations/available-slots?date=YYYY-MM-DD&partySize=4`
   - Response `200`:
     ```json
     {"success":true,"data":[{"time":"18:00:00","availableTables":3,"isAvailable":true}],"message":"Available slots retrieved successfully","count":1}
     ```
   - Error `400/500`: map with `success`, `message`, `errorCode`

2. `POST /reservations/book`
   - Request: `BookingRequestDto`
   - Success `201`: map with `success`, `message`, `bookingReference`, `confirmationCode`, reservation fields

3. `GET /reservations/{id}`
   - Response: `ReservationDto` or `404`

4. `GET /reservations/upcoming`
   - Response: `ReservationDto[]`

5. `GET /reservations/customer/{customerId}`
   - Response: `ReservationDto[]`

6. `GET /reservations/date/{date}`
   - Response: `ReservationDto[]`

7. `PUT /reservations/{id}`
   - Request: `BookingRequestDto`
   - Response: map `{success,message,reservationId,reservationDate,reservationTime}`

8. `DELETE /reservations/{id}`
   - Response: map `{success,message}`

9. `PATCH /reservations/{id}/status`
   - Request body: `{"status":"confirmed"}`
   - Response: map `{success,message,reservationId,newStatus}`

10. `GET /reservations/check-availability?tableId=1&date=YYYY-MM-DD&time=18:00:00&partySize=4`
   - Response: map `{available,tableId,date,time,partySize}`

## 6.5 Kitchen (`/api/kitchen`)
1. `GET /api/kitchen/orders`
   - Response: `KitchenOrder[]`

2. `GET /api/kitchen/waiters`
   - Response: `Waiter[]`

3. `GET /api/kitchen/open-orders`
   - Response: `Order[]`

4. `POST /api/kitchen/send?orderId={id}`
   - Response: empty `200`

5. `POST /api/kitchen/ready?orderId={id}`
   - Response: empty `200`

6. `POST /api/kitchen/assign`
   - Request: `AssignWaiterRequest`
   - Response: empty `200`

7. `GET /api/kitchen/assignments`
   - Response: `WaiterDetails[]`

## 6.6 Waiter (`/api/waiter`)
1. `POST /api/waiter/status`
   - Request: `UpdateOrderStatus`
   - Response: string `"Status updated to: ..."`

## 6.7 Supplier (`/api/supplier`)
1. `GET /api/supplier/get-all` -> `SupplierDto[]`
2. `POST /api/supplier/save` (body `SupplierDto`) -> `SupplierDto`
3. `PUT /api/supplier/update` (body `SupplierDto`) -> `SupplierDto`
4. `DELETE /api/supplier/delete/{id}` -> `boolean`

## 6.8 Menu categories (`/api/categories`)
1. `POST /api/categories` (`MenuCategoriesDto`) -> `boolean`
2. `PUT /api/categories` (`MenuCategoriesDto`) -> `boolean`
3. `DELETE /api/categories/{id}` -> `boolean`
4. `GET /api/categories/{id}` -> `MenuCategoriesDto`
5. `GET /api/categories/get-all` -> `MenuCategoriesDto[]`

## 6.9 Menu items (`/api/menu-items`)
1. `POST /api/menu-items` (`MenuItemsDto`) -> `boolean`
2. `PUT /api/menu-items` (`MenuItemsDto`) -> `boolean`
3. `DELETE /api/menu-items/{id}` -> `boolean`
4. `GET /api/menu-items/{id}` -> `MenuItemsDto`
5. `GET /api/menu-items` -> `MenuItemsDto[]`
6. `GET /api/menu-items/category/{categoryId}` -> `MenuItemsDto[]`
7. `GET /api/menu-items/available-items` -> `MenuItemsDto[]`

## 6.10 Menu item pricing (`/api/menu-item-price`)
1. `POST /api/menu-item-price` (`MenuItemPriceDto`) -> `boolean`
2. `PUT /api/menu-item-price` (`MenuItemPriceDto`) -> `boolean`
3. `DELETE /api/menu-item-price/{id}` -> `boolean`
4. `GET /api/menu-item-price/{id}` -> `MenuItemPriceDto`
5. `GET /api/menu-item-price/find/{itemId}` -> `MenuItemPriceDto[]`
6. `GET /api/menu-item-price/get-price/{itemId}` -> `MenuItemPriceDto[]`
7. `GET /api/menu-item-price/get-full-menu` -> `MenuItemPriceDto[]`

## 6.11 Portions (`/portions` and `/api/portions`)
1. `POST /api/portions` (`PortionsDto`) -> `boolean`
2. `PUT /api/portions` (`PortionsDto`) -> `boolean`
3. `DELETE /api/portions/{id}` -> `boolean`
4. `GET /api/portions/{id}` -> `PortionsDto`
5. `GET /api/portions` -> `PortionsDto[]`

## 6.12 Tables (`/tables` and `/api/tables`)
1. `GET /api/tables` -> `TableDto[]`

## 6.13 Ingredients (`/ingredient`)
1. `POST /ingredient` (`IngredientDto`) -> empty `200`
2. `GET /ingredient/{id}` -> `IngredientDto`
3. `GET /ingredient?page=0&size=10` -> `IngredientDto[]`
4. `PUT /ingredient/{id}` (`IngredientDto`) -> `IngredientDto`
5. `DELETE /ingredient/{id}` -> empty `200`

## 6.14 Order types (`/api/order-types`)
1. `GET /api/order-types` -> `OrderTypeDto[]`
2. `GET /api/order-types/active` -> `OrderTypeDto[]`
3. `GET /api/order-types/{id}` -> `OrderTypeDto` or `404`
4. `GET /api/order-types/by-name/{typeName}` -> `OrderTypeDto` or `404`

## 6.15 Simple role test endpoints
1. `GET /admin` -> `"Welcome Admin"`
2. `GET /user` -> `"Welcome User"`

## 6.16 Campaigns (`/api/campaigns`) [Admin]
1. `POST /api/campaigns` (`MarketingCampaignDto`) -> `201` + `MarketingCampaignDto` or error string
2. `GET /api/campaigns` -> `MarketingCampaignDto[]`
3. `GET /api/campaigns/{id}` -> `MarketingCampaignDto`
4. `PUT /api/campaigns/{id}` (`MarketingCampaignDto`) -> `MarketingCampaignDto`
5. `DELETE /api/campaigns/{id}` -> success/error string
6. `GET /api/campaigns/status/{status}` -> `MarketingCampaignDto[]`
7. `GET /api/campaigns/segment/{segmentId}` -> `MarketingCampaignDto[]`
8. `GET /api/campaigns/channel/{channel}` -> `MarketingCampaignDto[]`
9. `GET /api/campaigns/search?name=...` -> `MarketingCampaignDto[]`
10. `POST /api/campaigns/{id}/schedule?scheduledAt=2026-04-01T12:00:00` -> string
11. `POST /api/campaigns/{id}/send` -> string
12. `POST /api/campaigns/{id}/cancel` -> string

## 6.17 Campaign analytics (`/api/campaign-analytics`) [Admin]
1. `POST /api/campaign-analytics/{campaignId}/record-sent?customerId=1&variant=A` -> `CampaignAnalyticsDto`
2. `POST /api/campaign-analytics/{campaignId}/record-open?customerId=1` -> string
3. `POST /api/campaign-analytics/{campaignId}/record-click?customerId=1` -> string
4. `POST /api/campaign-analytics/{campaignId}/record-conversion?customerId=1` -> string
5. `POST /api/campaign-analytics/{campaignId}/record-unsubscribe?customerId=1` -> string
6. `GET /api/campaign-analytics/{campaignId}/metrics` -> `CampaignPerformanceMetricsDto`
7. `GET /api/campaign-analytics/metrics/all` -> `CampaignPerformanceMetricsDto[]`
8. `GET /api/campaign-analytics/{campaignId}/metrics/date-range?startDate=...&endDate=...` -> `CampaignPerformanceMetricsDto`
9. `GET /api/campaign-analytics/{campaignId}/all` -> `CampaignAnalyticsDto[]`
10. `GET /api/campaign-analytics/{campaignId}/customer/{customerId}` -> `CampaignAnalyticsDto[]`
11. `DELETE /api/campaign-analytics/{campaignId}/delete-analytics` -> string

## 6.18 Email (`/api/emails`) [Admin]
1. `POST /api/emails/send-marketing` (body `MarketingEmailRequest`) -> string
2. `POST /api/emails/send-birthday?recipientEmail=...&recipientName=...&discount=25` -> string
3. `POST /api/emails/send-anniversary?recipientEmail=...&recipientName=...&discount=20&years=5` -> string
4. `POST /api/emails/send-promotional?recipientEmail=...&recipientName=...&discount=15` -> string
5. `POST /api/emails/test?recipientEmail=...` -> string

## 6.19 Email scheduler (`/api/admin/email-scheduler`) [Admin]
1. `GET /api/admin/email-scheduler` -> `EmailSchedulerConfigDto`
2. `PUT /api/admin/email-scheduler` (body `EmailSchedulerConfigDto`) -> `EmailSchedulerConfigDto`

## 6.20 Automated messages (`/api/admin/automated-messages`) [Admin]
Message CRUD:
1. `GET /api/admin/automated-messages` -> `AutomatedMessage[]` (active)
2. `GET /api/admin/automated-messages/inactive` -> `AutomatedMessage[]`
3. `GET /api/admin/automated-messages/{id}` -> `AutomatedMessage`
4. `GET /api/admin/automated-messages/trigger/{triggerType}` -> `AutomatedMessage[]`
5. `POST /api/admin/automated-messages` (body `AutomatedMessage`) -> `201` + `id`
6. `PUT /api/admin/automated-messages/{id}` (body `AutomatedMessage`) -> empty `200`
7. `DELETE /api/admin/automated-messages/{id}` -> empty `200`
8. `PUT /api/admin/automated-messages/{id}/toggle` -> empty `200`

Email send endpoints:
9. `POST /api/admin/automated-messages/send-marketing` (body `MarketingEmailRequest`) -> string
10. `POST /api/admin/automated-messages/send-birthday` (query/body mixed) -> string
11. `POST /api/admin/automated-messages/send-birthday-request` (body `MarketingEmailRequest`) -> string
12. `POST /api/admin/automated-messages/send-anniversary` (query) -> string
13. `POST /api/admin/automated-messages/send-anniversary-request` (body `MarketingEmailRequest`) -> string
14. `POST /api/admin/automated-messages/send-promotional` (query) -> string
15. `POST /api/admin/automated-messages/send-promotional-request` (body `MarketingEmailRequest`) -> string
16. `POST /api/admin/automated-messages/test` (query) -> string
17. `POST /api/admin/automated-messages/test-request` (body `MarketingEmailRequest`) -> string

Scheduler and bulk:
18. `GET /api/admin/automated-messages/scheduler` -> `EmailSchedulerConfigDto`
19. `PUT /api/admin/automated-messages/scheduler` (body `EmailSchedulerConfigDto`) -> `EmailSchedulerConfigDto` or error string
20. `POST /api/admin/automated-messages/send-to-all` -> map `{status,message,emailsSent,failed,skipped}`

---

## 7) WebSocket Realtime Integration
## 7.1 Connection
- Endpoint: `/ws` (SockJS)
- Broker prefix: `/topic`
- App prefix: `/app`

## 7.2 Topics to subscribe
- `/topic/pos-updates`
- `/topic/kds-updates`
- `/topic/tablet-response`

## 7.3 Event payloads published by backend
From `WebSocketNotificationService`:

1. POS new order
```json
{
  "event": "NEW_TABLET_ORDER",
  "order": {"id": 101, "orderNumber": "ORD-..."},
  "timestamp": "2026-04-01T12:34:56"
}
```

2. KDS new order
```json
{
  "event": "NEW_KDS_ORDER",
  "kdsOrderId": 501,
  "orderNumber": "ORD-...",
  "itemCount": 3,
  "timestamp": "2026-04-01T12:34:56"
}
```

3. KDS item status update
```json
{
  "event": "KDS_ITEM_STATUS_UPDATE",
  "kdsOrderItemId": 9001,
  "status": "ready",
  "kdsOrderId": 501,
  "timestamp": "2026-04-01T12:34:56"
}
```

4. POS order completed
```json
{
  "event": "ORDER_COMPLETED",
  "orderId": 101,
  "orderNumber": "ORD-...",
  "timestamp": "2026-04-01T12:34:56"
}
```

5. Tablet success/error
```json
{"status":"SUCCESS","orderId":101,"orderNumber":"ORD-...","message":"Order placed","timestamp":"..."}
```
```json
{"status":"ERROR","message":"Validation failed","errorCode":"INVALID_REQUEST","timestamp":"..."}
```

6. KDS snapshot
```json
{
  "event": "KDS_ORDERS_SNAPSHOT",
  "orders": [],
  "timestamp": "2026-04-01T12:34:56"
}
```

---

## 8) Copilot Prompt Playbook (for faster frontend generation)
Use these prompts directly in your frontend repo.

1. Generate typed API client
- "Create a TypeScript Axios client with interceptor for Bearer token and 401 logout handling. Base URL from `VITE_API_BASE_URL`."

2. Generate auth module
- "Create `auth.api.ts`, `auth.store.ts`, and `LoginPage.tsx` using `/api/auth/login` and `/api/auth/register` with `AuthResponse` typing."

3. Generate orders module
- "Create orders hooks and pages for list/open/create using `/api/order` endpoints and `OrderResponse`, `OrderCreateRequest`, `TabletOrderRequest`."

4. Generate reservations module
- "Create a 4-step reservation wizard: available slots -> customer details -> confirmation -> result using `/reservations` endpoints."

5. Generate websocket client
- "Create `stomp.ts` using SockJS+STOMP and subscribe to `/topic/pos-updates`, `/topic/kds-updates`, `/topic/tablet-response` with reconnect and handlers."

6. Generate admin campaigns
- "Create campaign CRUD pages with filters and analytics dashboard using `/api/campaigns` and `/api/campaign-analytics` endpoints."

---

## 9) Implementation Notes and Edge Cases
- Some controllers return primitive/string/map responses. Keep response parser flexible.
- Reservation endpoints exist on both `/reservations` and `/api/reservations`; choose one base consistently.
- Several email and automated-message endpoints are similar; centralize common request builders.
- Use optimistic UI only where backend is deterministic (for example order status updates).
- For all `LocalDateTime` params in query, send ISO format (example: `2026-04-01T14:30:00`).
- Keep role checks both in route guards and in UI action controls.

---

## 10) Quick Start Checklist for Frontend Team
- [ ] Implement API client with token interceptor
- [ ] Implement auth store + protected routing
- [ ] Add typed DTOs from section 5
- [ ] Build POS module (orders + menu + tables)
- [ ] Build Kitchen module (orders + waiter assignment)
- [ ] Build Reservation module (slot booking flow)
- [ ] Build CRM module (customers)
- [ ] Build Admin module (campaigns, analytics, email automation)
- [ ] Add WebSocket live updates
- [ ] Add error normalization using `ErrorResponse`

This documentation reflects the current backend contracts in the workspace and is ready for Copilot-assisted frontend generation.

