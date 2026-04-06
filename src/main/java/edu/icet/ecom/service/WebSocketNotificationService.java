package edu.icet.ecom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private static final String POS_UPDATES_TOPIC = "/topic/pos-updates";
    private static final String KDS_UPDATES_TOPIC = "/topic/kds-updates";
    private static final String TABLET_RESPONSE_TOPIC = "/topic/tablet-response";
    private static final String INVENTORY_UPDATES_TOPIC = "/topic/inventory-updates";

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Broadcast new order to POS system
     */
    public void notifyPOSNewOrder(Object orderData) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "NEW_TABLET_ORDER");
        payload.put("order", orderData);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(POS_UPDATES_TOPIC, (Object) payload);
    }

    /**
     * Broadcast new KDS order to Kitchen Display System
     */
    public void notifyKDSNewOrder(Integer kdsOrderId, String orderNumber, int itemCount) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "NEW_KDS_ORDER");
        payload.put("kdsOrderId", kdsOrderId);
        payload.put("orderNumber", orderNumber);
        payload.put("itemCount", itemCount);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(KDS_UPDATES_TOPIC, (Object) payload);
    }

    /**
     * Broadcast KDS item status update to Kitchen Display
     */
    public void notifyKDSItemStatusUpdate(Integer kdsOrderItemId, String status, Integer kdsOrderId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "KDS_ITEM_STATUS_UPDATE");
        payload.put("kdsOrderItemId", kdsOrderItemId);
        payload.put("status", status);
        payload.put("kdsOrderId", kdsOrderId);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(KDS_UPDATES_TOPIC, (Object) payload);
    }

    /**
     * Broadcast order completion to POS
     */
    public void notifyPOSOrderCompleted(Integer orderId, String orderNumber) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "ORDER_COMPLETED");
        payload.put("orderId", orderId);
        payload.put("orderNumber", orderNumber);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(POS_UPDATES_TOPIC, (Object) payload);
    }

    /**
     * Send order confirmation to tablet device
     */
    public void notifyTabletOrderConfirmed(Integer orderId, String orderNumber, String message) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "SUCCESS");
        payload.put("orderId", orderId);
        payload.put("orderNumber", orderNumber);
        payload.put("message", message);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(TABLET_RESPONSE_TOPIC, (Object) payload);
    }

    /**
     * Send order error to tablet device
     */
    public void notifyTabletOrderError(String message, String errorCode) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("status", "ERROR");
        payload.put("message", message);
        payload.put("errorCode", errorCode);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(TABLET_RESPONSE_TOPIC, (Object) payload);
    }

    /**
     * Broadcast inventory stock change to connected clients
     */
    public void notifyInventoryStockUpdate(Integer ingredientId, BigDecimal newStock, String unit) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "INVENTORY_DEDUCTED");
        payload.put("ingredientId", ingredientId);
        payload.put("newStock", newStock);
        payload.put("unit", unit);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(INVENTORY_UPDATES_TOPIC, (Object) payload);
    }
    public void notifyLowStockAlert(Integer ingredientId,
                                    String ingredientName,
                                    BigDecimal currentStock,
                                    String unit,
                                    BigDecimal threshold,
                                    String reorderLink) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "LOW_STOCK_ALERT");
        payload.put("ingredientId", ingredientId);
        payload.put("ingredientName", ingredientName);
        payload.put("currentStock", currentStock);
        payload.put("unit", unit);
        payload.put("threshold", threshold);
        payload.put("reorderLink", reorderLink);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(INVENTORY_UPDATES_TOPIC, (Object) payload);
    }

    /**
     * Broadcast all KDS pending orders to Kitchen
     */
    public void notifyKDSOrdersSnapshot(List<?> orders) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("event", "KDS_ORDERS_SNAPSHOT");
        payload.put("orders", orders);
        payload.put("timestamp", LocalDateTime.now());
        messagingTemplate.convertAndSend(KDS_UPDATES_TOPIC, (Object) payload);
    }
}

