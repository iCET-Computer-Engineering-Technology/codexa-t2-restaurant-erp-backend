package edu.icet.ecom.repository;

import edu.icet.ecom.entity.KdsOrder;
import java.util.List;

public interface KdsRepository {
    Integer saveAndGetId(KdsOrder kdsOrder);
    Integer saveKdsOrderItem(Integer kdsOrderId, Integer orderItemId);
    KdsOrder findByOrderId(Integer orderId);
    List<KdsOrder> findAll();
    boolean updateKdsItemStatus(Integer kdsOrderItemId, String status);
    List<KdsOrder> findAllPending();
}

