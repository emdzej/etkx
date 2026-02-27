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
@Table(name = "w_hgfg")
@IdClass(HgFgId.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HgFg {

    @Id
    @Column(name = "hgfg_hg", length = 2, columnDefinition = "CHAR(2)")
    private String hgfgHg;

    @Id
    @Column(name = "hgfg_fg", length = 2, columnDefinition = "CHAR(2)")
    private String hgfgFg;

    @Id
    @Column(name = "hgfg_produktart", length = 1, columnDefinition = "CHAR(1)")
    private String hgfgProduktart;

    @Column(name = "hgfg_textcode")
    private Integer hgfgTextcode;

    @Column(name = "hgfg_bereich", length = 20)
    private String hgfgBereich;

    @Column(name = "hgfg_grafikid")
    private Integer hgfgGrafikid;

    @Column(name = "hgfg_ist_valueline", length = 1, columnDefinition = "CHAR(1)")
    private String hgfgIstValueline;
}
