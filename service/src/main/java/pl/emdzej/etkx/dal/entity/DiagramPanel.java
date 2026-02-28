package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a diagram panel (exploded view) in the parts catalog.
 * Maps to the w_bildtaf table containing diagram metadata and grouping.
 */
@Entity
@Table(name = "w_bildtaf")
@Data
public class DiagramPanel {
    /**
     * Diagram number (Bildtafel number).
     */
    @Id
    @Column(name = "bildtaf_btnr")
    private String diagramNumber;

    /**
     * Main group code (HG).
     */
    @Column(name = "bildtaf_hg")
    private String mainGroupCode;

    /**
     * Function group code (FG).
     */
    @Column(name = "bildtaf_fg")
    private String functionGroupCode;

    /**
     * Graphic/image identifier.
     */
    @Column(name = "bildtaf_grafikid")
    private String graphicId;

    /**
     * Brand identifier for the diagram.
     */
    @Column(name = "bildtaf_marke")
    private String brand;

    /**
     * Area or section identifier.
     */
    @Column(name = "bildtaf_bereich")
    private String area;

    /**
     * Diagram type code.
     */
    @Column(name = "bildtaf_bteart")
    private String diagramType;

    /**
     * Condition code for applicability.
     */
    @Column(name = "bildtaf_bedkez")
    private String conditionCode;

    /**
     * Text code used to resolve localized title.
     */
    @Column(name = "bildtaf_textc")
    private String textCode;

    /**
     * Diagram position or sort order.
     */
    @Column(name = "bildtaf_pos")
    private String position;

    /**
     * Country code (LKZ).
     */
    @Column(name = "bildtaf_lkz")
    private String countryCode;

    /**
     * Security flag.
     */
    @Column(name = "bildtaf_sicher")
    private String securityFlag;
}
