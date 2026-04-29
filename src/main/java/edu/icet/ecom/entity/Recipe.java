package edu.icet.ecom.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private Integer id;
    private Integer versionNumber;
    private Integer isCurrent;
    private String notes;
    private Integer createdBy;
    private LocalDateTime createdAt;
}
