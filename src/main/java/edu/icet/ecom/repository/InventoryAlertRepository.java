package edu.icet.ecom.repository;

import edu.icet.ecom.dto.LowStockAlertDto;

import java.util.List;

public interface InventoryAlertRepository {
    boolean existsActiveLowStockAlert(Integer ingredientId);
    void createLowStockAlert(Integer ingredientId);
    void resolveLowStockAlerts(Integer ingredientId);
    List<LowStockAlertDto> findActiveLowStockAlerts();
}

