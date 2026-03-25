package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.SupplierDto;
import edu.icet.ecom.entity.Supplier;
import edu.icet.ecom.repository.SupplierRepository;
import edu.icet.ecom.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    @Override
    public List<SupplierDto> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.getAllSuppliers();

        return suppliers.stream().map(supplier -> {
            SupplierDto dto = new SupplierDto();
            dto.setId(supplier.getId());
            dto.setName(supplier.getName());
            dto.setContactName(supplier.getContactName());
            dto.setEmail(supplier.getEmail());
            dto.setPhone(supplier.getPhone());
            dto.setAddress(supplier.getAddress());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public SupplierDto saveSupplier(SupplierDto supplierDto) {
        return supplierRepository.saveSupplier(supplierDto);
    }

    @Override
    public SupplierDto updateSupplier(SupplierDto supplierDto) {
        return supplierRepository.updateSupplier(supplierDto);
    }

    @Override
    public boolean deleteSupplier(Integer id) {
        return supplierRepository.deleteSupplier(id);
    }
}
