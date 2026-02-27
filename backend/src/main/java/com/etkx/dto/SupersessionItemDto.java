package com.etkx.dto;

import java.time.LocalDate;
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
public class SupersessionItemDto {

    private Integer nr;
    private String sachnr;
    private String description;
    private LocalDate expiration;
}
