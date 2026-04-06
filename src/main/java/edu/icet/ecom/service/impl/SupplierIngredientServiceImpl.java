package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.IngredientForSupplierDto;
import edu.icet.ecom.dto.SupplierWithIngredientsDto;
import edu.icet.ecom.repository.SupplierIngredientRepository;
import edu.icet.ecom.service.SupplierIngredientService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SupplierIngredientServiceImpl implements SupplierIngredientService {

    private final SupplierIngredientRepository supplierIngredientRepository;

    @Override
    public List<SupplierWithIngredientsDto> getAllSuppliersWithIngredients() {
        return supplierIngredientRepository.findAllSuppliersWithIngredients();
    }

    @Override
    public List<IngredientForSupplierDto> getIngredientsBySupplier(Long supplierId) {
        return supplierIngredientRepository.findIngredientsBySupplierId(supplierId);
    }
}
