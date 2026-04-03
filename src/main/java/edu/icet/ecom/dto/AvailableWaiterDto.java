package edu.icet.ecom.dto;

import edu.icet.ecom.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvailableWaiterDto {
    private UserEntity waiter;
    private int activeOrdersCount;
}

