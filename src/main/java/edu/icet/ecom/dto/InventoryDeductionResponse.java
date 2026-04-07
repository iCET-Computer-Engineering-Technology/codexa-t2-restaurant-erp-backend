package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryDeductionResponse {
    private boolean deducted;
    private String message;
}

