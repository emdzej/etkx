package com.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "w_btzeilen")
@IdClass(BtZeilenId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BtZeilen {

    @Id
    @Column(name = "btzeilen_btnr", length = 7, columnDefinition = "CHAR(7)")
    private String btzeilenBtnr;

    @Id
    @Column(name = "btzeilen_pos")
    private Integer btzeilenPos;

    @Column(name = "btzeilen_bildposnr", length = 5)
    private String btzeilenBildposnr;

    @Column(name = "btzeilen_sachnr", length = 20)
    private String btzeilenSachnr;

    @Column(name = "btzeilen_kommbt")
    private Integer btzeilenMenge;
}
