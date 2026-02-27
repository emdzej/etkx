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
public class SubGroupDto {

    private String fg;
    private String name;
    private String thumbnailUrl;
    private String diagramNumber;
}
