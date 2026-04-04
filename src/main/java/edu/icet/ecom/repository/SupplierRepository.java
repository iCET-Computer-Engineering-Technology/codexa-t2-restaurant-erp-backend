package edu.icet.ecom.repository;

import edu.icet.ecom.dto.SupplierDto;
import edu.icet.ecom.entity.Supplier;

import java.util.List;

public interface SupplierRepository {
    List<Supplier> getAllSuppliers();
    SupplierDto saveSupplier(SupplierDto supplierDto);
    SupplierDto updateSupplier(SupplierDto supplierDto);
    boolean deleteSupplier(Integer id);
}
