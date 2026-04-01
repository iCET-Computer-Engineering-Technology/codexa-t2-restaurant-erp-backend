package edu.icet.ecom.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Chef {
    private Long id;
    private String name;
    private String availability;
    private Integer currentTaskLoad;
}
