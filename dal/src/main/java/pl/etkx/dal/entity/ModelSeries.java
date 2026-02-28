package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a model series definition used to group vehicles by series and market attributes.
 * Maps to the w_baureihe table containing model series metadata.
 */
@Entity
@Table(name = "w_baureihe")
@Data
public class ModelSeries {
    /**
     * Product type (Produktart).
     */
    @Column(name = "baureihe_produktart")
    private String productType;

    /**
     * Body construction type (Bauart).
     */
    @Column(name = "baureihe_bauart")
    private String bodyType;

    /**
     * Sales area (Verkaufsbereich).
     */
    @Column(name = "baureihe_vbereich")
    private String salesArea;

    /**
     * Text code used to resolve the series name.
     */
    @Column(name = "baureihe_textcode")
    private Integer textCode;

    /**
     * Position for ordering within a list.
     */
    @Column(name = "baureihe_position")
    private Integer position;

    /**
     * Model series code (Baureihe).
     */
    @Id
    @Column(name = "baureihe_baureihe")
    private String modelSeries;

    /**
     * Series area or range (Bereich).
     */
    @Column(name = "baureihe_bereich")
    private String area;

    /**
     * Brand identifier (Marke TPS).
     */
    @Column(name = "baureihe_marke_tps")
    private String brandTps;

    /**
     * Graphic identifier for the series icon.
     */
    @Column(name = "baureihe_grafikid")
    private Integer graphicId;
}
