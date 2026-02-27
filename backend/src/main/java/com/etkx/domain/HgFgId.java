package com.etkx.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HgFgId implements Serializable {

    private String hgfgHg;
    private String hgfgFg;
    private String hgfgProduktart;
}
