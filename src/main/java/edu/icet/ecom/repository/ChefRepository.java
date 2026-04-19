package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Chef;

import java.util.List;

public interface ChefRepository {
    List<Chef> findAvailableChefs();

    Chef findById(Long id);

    void increaseTaskLoad(Long chefId);

    void decreaseTaskLoad(Long chefId);
}
