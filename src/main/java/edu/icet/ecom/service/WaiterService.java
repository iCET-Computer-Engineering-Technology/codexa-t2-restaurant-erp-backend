package com.codexa.retauranterp.service;

import edu.icet.ecom.entity.OrderAssigment;

import java.util.List;

public interface WaiterService {
    List<OrderAssigment> getUnservedOrders();


}
