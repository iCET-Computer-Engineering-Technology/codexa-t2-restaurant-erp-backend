package edu.icet.ecom.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PortionSizeDto {
    private Integer id;
    private String sizeName;
    private String description;
    private Timestamp createdAt;
}
