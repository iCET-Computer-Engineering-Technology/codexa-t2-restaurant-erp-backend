package edu.icet.ecom.controller;

import edu.icet.ecom.dto.SupplierDto;
import edu.icet.ecom.service.SupplierService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@RequiredArgsConstructor
@CrossOrigin
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/get-all")
    public List<SupplierDto> getAllSuppliers(){
        return supplierService.getAllSuppliers();
    }
    @PostMapping("/save")
    public SupplierDto saveSupplier(@RequestBody SupplierDto supplierDto){
        return supplierService.saveSupplier(supplierDto);
    }
    @PutMapping("/update")
    public SupplierDto updateSupplier(@RequestBody SupplierDto supplierDto) {
        return supplierService.updateSupplier(supplierDto);
    }
    @DeleteMapping("/delete/{id}")
    public boolean deleteSupplier(@PathVariable Integer id){
        return supplierService.deleteSupplier(id);
    }
}
