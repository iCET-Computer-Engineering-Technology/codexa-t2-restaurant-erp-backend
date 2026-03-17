package edu.icet.ecom.service;

import edu.icet.ecom.dto.WaiterOrderDto;
import edu.icet.ecom.entity.Waiters;

import java.util.List;

public interface WaitersService {
    List<Waiters> getActiveWaiters();

    List<WaiterOrderDto> getUnservedOrders(Integer waiterId);

    boolean serveOrder(Integer orderId);
}
