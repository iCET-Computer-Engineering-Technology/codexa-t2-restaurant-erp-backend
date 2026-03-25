package edu.icet.ecom.dto;

import lombok.Data;

@Data
public class SupplierDto {
    private Integer id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;
}
