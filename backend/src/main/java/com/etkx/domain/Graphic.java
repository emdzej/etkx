package com.etkx.domain;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "w_grafik")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Graphic {

    @Id
    @Column(name = "grafik_grafikid")
    private Integer graphicId;

    @Column(name = "grafik_art", length = 1, columnDefinition = "CHAR(1)")
    private String graphicType;

    @Column(name = "grafik_format", length = 3, columnDefinition = "CHAR(3)")
    private String format;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "grafik_blob")
    private byte[] blob;
}
