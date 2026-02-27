package pl.emdzej.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "w_teil")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Part {

    @Id
    @Column(name = "teil_sachnr", length = 7)
    private String partNumber;

    @Column(name = "teil_hauptgr", length = 2)
    private String mainGroup;

    @Column(name = "teil_untergrup", length = 2)
    private String subGroup;

    @Column(name = "teil_textcode")
    private Integer textCode;

    @Column(name = "teil_benennzus", length = 30)
    private String supplement;

    @Column(name = "teil_art", length = 1)
    private String type;

    @Column(name = "teil_mengeeinh", length = 2)
    private String quantityUnit;

    @Column(name = "teil_alt", length = 7)
    private String oldPart;

    @Column(name = "teil_tausch", length = 7)
    private String replacement;
}
