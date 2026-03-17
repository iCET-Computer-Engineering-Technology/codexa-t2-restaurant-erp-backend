package edu.icet.ecom.repository;

import edu.icet.ecom.entity.WaiterOrder;
import edu.icet.ecom.entity.Waiters;

import java.util.Arrays;
import java.util.List;

public interface WaitersRepository {
    List<Waiters> getActiveWaiters();

    List<WaiterOrder> getUnservedOrders(Integer waiterId);

    boolean serveOrder(Integer orderId);
}
