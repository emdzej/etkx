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
@Table(name = "w_baureihe")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Baureihe {

    @Id
    @Column(name = "baureihe_baureihe", length = 10)
    private String baureiheBaureihe;

    @Column(name = "baureihe_produktart", length = 1)
    private String baureiheProduktart;

    @Column(name = "baureihe_bauart", length = 2)
    private String baureiheBauart;

    @Column(name = "baureihe_vbereich", length = 2)
    private String baureiheVbereich;

    @Column(name = "baureihe_textcode")
    private Integer baureiheTextcode;

    @Column(name = "baureihe_position")
    private Integer baureihePosition;

    @Column(name = "baureihe_bereich", length = 10)
    private String baureiheBereich;

    @Column(name = "baureihe_marke_tps", length = 10)
    private String baureiheMarkeTps;

    @Column(name = "baureihe_grafikid")
    private Integer baureiheGrafikid;
}
