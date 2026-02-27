package com.etkx.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BtZeilenId implements Serializable {

    private String btzeilenBtnr;
    private Integer btzeilenPos;
}
