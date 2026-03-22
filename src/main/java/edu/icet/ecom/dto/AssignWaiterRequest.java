package edu.icet.ecom.dto;

public class AssignWaiterRequest {
    private Long kitchenOrderId;
    private Long waiterId;

    public Long getKitchenOrderId() {
        return kitchenOrderId;
    }

    public void setKitchenOrderId(Long kitchenOrderId) {
        this.kitchenOrderId = kitchenOrderId;
    }

    public Long getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(Long waiterId) {
        this.waiterId = waiterId;
    }
}
