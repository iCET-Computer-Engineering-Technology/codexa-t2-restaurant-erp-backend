package edu.icet.ecom.service;


import edu.icet.ecom.entity.Bonus;

import java.util.List;

public interface BonusService {
    List<Bonus> getBonus();
    Boolean addBonus(Bonus bonus);
    Boolean updateBonus(Bonus bonus);
    void deleteBonus(Integer id);
}
