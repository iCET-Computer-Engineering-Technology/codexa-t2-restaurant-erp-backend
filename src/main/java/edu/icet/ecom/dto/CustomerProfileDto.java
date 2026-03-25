package edu.icet.ecom.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerProfileDto {
    private Integer customerId;
    private String fullName;
    private String phone;
    private String dietaryNotes;
    private Integer loyaltyPoints;
    private Double lifetimeSpend;
    private List<String> favoriteItems;
    private List<Visitdto> last10Visits;
}
