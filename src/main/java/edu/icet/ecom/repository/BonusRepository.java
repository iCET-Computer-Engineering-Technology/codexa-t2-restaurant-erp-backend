package edu.icet.ecom.repository;

import edu.icet.ecom.entity.Bonus;

import java.util.List;

public interface BonusRepository {
    List<Bonus> getBonus();
    Boolean addBonus(Bonus bonus);
    Boolean updateBonus(Bonus bonus);
    void deleteBonus(Integer id);
    List<Bonus> searchBonusById(Integer id);
}
