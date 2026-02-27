package com.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "w_publben")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Publben {

    @Id
    @Column(name = "publben_textcode")
    private Integer publbenTextcode;

    @Transient
    private String publbenSprache;

    @Column(name = "publben_bezeichnung", length = 80)
    private String publbenText;
}
