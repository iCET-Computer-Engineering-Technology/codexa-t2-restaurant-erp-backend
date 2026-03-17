package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ModifierDto;
import edu.icet.ecom.repository.ModifierRepositery;
import edu.icet.ecom.service.ModifierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifierServiceImpl implements ModifierService {

    private final ModifierRepositery repositery;

    @Override
    public boolean addModifier(ModifierDto modifierDto) {
        return repositery.addModifier(modifierDto);
    }

    @Override
    public boolean updateModifier(ModifierDto modifierDto) {
        return repositery.updateModifier(modifierDto);
    }

    @Override
    public boolean deleteModifierById(Integer id) {
        return repositery.deleteModifierById(id);
    }

    @Override
    public List<ModifierDto> getModifiersByGroupId(Integer groupId) {
        return repositery.getModifiersByGroupId(groupId);
    }
}
