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
public class PartUsageDto {

    private String diagramNumber;
    private String position;
    private String mainGroup;
    private String subGroup;
}
