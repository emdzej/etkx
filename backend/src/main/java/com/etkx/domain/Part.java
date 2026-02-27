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
@Table(name = "w_teil")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @Column(name = "teil_sachnr", length = 7, columnDefinition = "CHAR(7)")
    private String partNumber;

    @Column(name = "teil_hauptgr", length = 2, columnDefinition = "CHAR(2)")
    private String mainGroup;

    @Column(name = "teil_untergrup", length = 2, columnDefinition = "CHAR(2)")
    private String subGroup;

    @Column(name = "teil_textcode")
    private Integer textCode;

    @Column(name = "teil_benennzus", length = 30)
    private String descriptionSuffix;

    @Column(name = "teil_art", length = 1, columnDefinition = "CHAR(1)")
    private String partType;
}
