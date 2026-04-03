package edu.icet.ecom.service;

import edu.icet.ecom.dto.SupplierDto;

import java.util.List;

public interface SupplierService {
    List<SupplierDto> getAllSuppliers();
    SupplierDto saveSupplier(SupplierDto supplierDto);
    SupplierDto updateSupplier(SupplierDto supplierDto);
    boolean deleteSupplier(Integer id);
}
