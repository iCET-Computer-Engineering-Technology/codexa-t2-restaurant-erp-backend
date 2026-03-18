package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.ModifiersDto;
import edu.icet.ecom.repository.ModifiersRepository;
import edu.icet.ecom.service.ModifiersService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModifiersServiceImpl implements ModifiersService {

    private final ModifiersRepository repositery;

    @Override
    public boolean addModifier(ModifiersDto modifierDto) {
        return repositery.addModifier(modifierDto);
    }

    @Override
    public boolean updateModifier(ModifiersDto modifierDto) {
        return repositery.updateModifier(modifierDto);
    }

    @Override
    public boolean deleteModifierById(Integer id) {
        return repositery.deleteModifierById(id);
    }

    @Override
    public List<ModifiersDto> getModifiersByGroupId(Integer groupId) {
        return repositery.getModifiersByGroupId(groupId);
    }
}
