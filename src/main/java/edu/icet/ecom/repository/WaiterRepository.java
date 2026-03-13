package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Waiter;

import java.util.List;

public interface WaiterRepository {

    List<Waiter> findActiveWaiters();

}
