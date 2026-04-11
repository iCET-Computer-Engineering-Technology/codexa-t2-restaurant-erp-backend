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
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dietaryNotes;
    private Double loyaltyPoints;
    private Double lifetimeSpend;
    private List<VisitHistoryDto> favoriteItems;
    private List<FavoriteItemDto> last10Visits;
    private List<VisitHistoryDto> recentVisits;
}
