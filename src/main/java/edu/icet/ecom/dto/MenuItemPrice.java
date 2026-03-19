package edu.icet.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MenuItemPrice {
    private Integer id;
    private Integer itemId;
    private Integer portion_id;
    private Double price;
    private boolean isActive;
}
