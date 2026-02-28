package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a diagram panel (Bildtafel) in the parts catalog.
 * Maps to the w_bildtaf table containing diagram metadata.
 * 
 * Diagrams show exploded views of assemblies with numbered parts.
 */
@Entity
@Table(name = "w_bildtaf")
@Data
public class DiagramPanel {
    /**
     * Diagram panel number (Bildtafelnummer).
     */
    @Id
    @Column(name = "bildtaf_btnr")
    private String diagramNumber;

    /**
     * Main group (Hauptgruppe).
     */
    @Column(name = "bildtaf_hg")
    private String mainGroup;

    /**
     * Sub group (Feingruppe).
     */
    @Column(name = "bildtaf_fg")
    private String subGroup;

    /**
     * Product type.
     */
    @Column(name = "bildtaf_produktart")
    private String productType;

    /**
     * Sales area (Verkaufsbereich).
     */
    @Column(name = "bildtaf_vbereich")
    private String salesArea;

    /**
     * Diagram type (Bildtafelart).
     */
    @Column(name = "bildtaf_bteart")
    private String diagramType;

    /**
     * Security flag.
     */
    @Column(name = "bildtaf_sicher")
    private String securityFlag;

    /**
     * Text code for localized diagram name.
     */
    @Column(name = "bildtaf_textc")
    private Integer textCode;

    /**
     * Reference to graphic in w_grafik table.
     */
    @Column(name = "bildtaf_grafikid")
    private Integer graphicId;

    /**
     * Display position.
     */
    @Column(name = "bildtaf_pos")
    private Integer position;

    /**
     * Comment diagram reference.
     */
    @Column(name = "bildtaf_kommbt")
    private Integer commentDiagram;

    /**
     * Condition code (Bedingungskennzeichen).
     */
    @Column(name = "bildtaf_bedkez")
    private String conditionCode;

    /**
     * Area/region.
     */
    @Column(name = "bildtaf_bereich")
    private String area;

    /**
     * CP available flag.
     */
    @Column(name = "bildtaf_vorh_cp")
    private String cpAvailable;

    /**
     * Country code.
     */
    @Column(name = "bildtaf_lkz")
    private String countryCode;

    /**
     * ValueLine diagram flag.
     */
    @Column(name = "bildtaf_ist_valueline")
    private String valueLineFlag;
}
