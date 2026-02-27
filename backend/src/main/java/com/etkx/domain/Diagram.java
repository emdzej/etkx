package com.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "w_bildtaf")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Diagram {

    @Id
    @Column(name = "bildtaf_btnr", length = 7, columnDefinition = "CHAR(7)")
    private String diagramNumber;

    @Column(name = "bildtaf_hg", length = 2, columnDefinition = "CHAR(2)")
    private String mainGroup;

    @Column(name = "bildtaf_fg", length = 2, columnDefinition = "CHAR(2)")
    private String subGroup;

    @Column(name = "bildtaf_produktart", length = 1, columnDefinition = "CHAR(1)")
    private String productType;

    @Column(name = "bildtaf_textc")
    private Integer textCode;

    @Column(name = "bildtaf_grafikid")
    private Integer graphicId;
}
