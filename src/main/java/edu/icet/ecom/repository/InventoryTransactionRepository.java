package edu.icet.ecom.repository;

import edu.icet.ecom.model.InventoryDeductionError;

import java.math.BigDecimal;

public interface InventoryTransactionRepository {
    void insertDeductionTransaction(Integer ingredientId, BigDecimal quantity, BigDecimal balanceAfter, Integer orderId, String notes);
    void insertDeductionError(InventoryDeductionError error);
    boolean wasOrderItemAlreadyDeducted(Integer orderItemId);
    boolean markOrderItemDeducted(Integer orderItemId);
}

