package edu.icet.ecom.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private Integer id;
    private String name;
    private String contactName;
    private String email;
    private String phone;
    private String address;
}
