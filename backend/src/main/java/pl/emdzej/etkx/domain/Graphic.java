package pl.emdzej.etkx.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "w_grafik")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Graphic {

    @Id
    @Column(name = "grafik_grafikid")
    private Integer graphicId;

    @Column(name = "grafik_laenge")
    private Integer length;

    @Column(name = "grafik_art", length = 1)
    private String type;

    @Column(name = "grafik_format", length = 3)
    private String format;

    @Lob
    @Column(name = "grafik_blob")
    private byte[] imageData;
}
