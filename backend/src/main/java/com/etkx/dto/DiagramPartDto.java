package com.etkx.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramPartDto {

    private String pos;
    private String sachnr;
    private Integer quantity;
    private String name;
}
