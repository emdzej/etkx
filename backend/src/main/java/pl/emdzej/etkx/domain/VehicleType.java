package pl.emdzej.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "w_fztyp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {

    @Id
    @Column(name = "fztyp_mospid")
    private Integer modelSeriesProdId;

    @Column(name = "fztyp_typschl", length = 4)
    private String typeKey;

    @Column(name = "fztyp_lenkung", length = 1)
    private String steering;

    @Column(name = "fztyp_getriebe", length = 1)
    private String transmission;

    @Column(name = "fztyp_baureihe", length = 4)
    private String series;

    @Column(name = "fztyp_motor", length = 5)
    private String engine;

    @Column(name = "fztyp_karosserie", length = 10)
    private String bodyType;

    @Column(name = "fztyp_vbez", length = 90)
    private String description;
}
