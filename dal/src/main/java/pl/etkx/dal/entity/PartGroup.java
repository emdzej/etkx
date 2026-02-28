package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a parts group hierarchy (Hauptgruppe/Feingruppe).
 * Maps to the w_hgfg table containing main and sub group definitions.
 * 
 * Main groups (HG) organize parts into major categories.
 * Sub groups (FG) further divide main groups into specific areas.
 */
@Entity
@Table(name = "w_hgfg")
@IdClass(PartGroupId.class)
@Data
public class PartGroup {
    /**
     * Main group code (Hauptgruppe).
     */
    @Id
    @Column(name = "hgfg_hg")
    private String mainGroup;

    /**
     * Sub group code (Feingruppe). 
     * Empty string for main group entries.
     */
    @Id
    @Column(name = "hgfg_fg")
    private String subGroup;

    /**
     * Product type code.
     */
    @Column(name = "hgfg_produktart")
    private String productType;

    /**
     * Text code for localized group name.
     */
    @Column(name = "hgfg_textcode")
    private Integer textCode;

    /**
     * Area/region code.
     */
    @Column(name = "hgfg_bereich")
    private String area;

    /**
     * Reference to group graphic.
     */
    @Column(name = "hgfg_grafikid")
    private Integer graphicId;

    /**
     * ValueLine group flag.
     */
    @Column(name = "hgfg_ist_valueline")
    private String valueLineFlag;
}
