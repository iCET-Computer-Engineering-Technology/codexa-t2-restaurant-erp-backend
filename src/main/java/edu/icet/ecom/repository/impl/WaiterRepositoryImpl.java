package edu.icet.ecom.repository.impl;

import edu.icet.ecom.entity.Waiter;
import edu.icet.ecom.repository.WaiterRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WaiterRepositoryImpl implements WaiterRepository {
    @Override
    public List<Waiter> findActiveWaiters() {
        return List.of();
    }
}
