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
public class VehicleDto {

    private Integer mospid;
    private String series;
    private String body;
    private String engine;
    private String steering;
    private String transmission;
}
