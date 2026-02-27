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
public class PartDetailsDto {

    private String sachnr;
    private String name;
    private String mainGroup;
    private String subGroup;
    private Integer textCode;
    private String descriptionSuffix;
    private String partType;
}
