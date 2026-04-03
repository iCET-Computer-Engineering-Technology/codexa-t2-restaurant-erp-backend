package edu.icet.ecom.controller;

import edu.icet.ecom.dto.IngredientForSupplierDto;
import edu.icet.ecom.dto.SupplierWithIngredientsDto;
import edu.icet.ecom.service.SupplierIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplier-ingredient")
@CrossOrigin
@RequiredArgsConstructor
public class SupplierIngredientController {

    private final SupplierIngredientService supplierIngredientService;

    @GetMapping
    public List<SupplierWithIngredientsDto> getAllSuppliersWithIngredients() {
        return supplierIngredientService.getAllSuppliersWithIngredients();
    }

    @GetMapping("/{supplierId}")
    public List<IngredientForSupplierDto> getIngredientsBySupplier(@PathVariable Long supplierId) {
        return supplierIngredientService.getIngredientsBySupplier(supplierId);
    }
}
