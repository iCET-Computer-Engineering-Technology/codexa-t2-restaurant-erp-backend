package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.SupplierDto;
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
        return supplierRepository.getAllSuppliers();
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
