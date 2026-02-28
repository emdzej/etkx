package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a main group (HG) in the catalog hierarchy.
 * Maps to the w_hgfg table with group metadata used for navigation.
 * 
 * Note: w_hgfg has composite PK (hgfg_hg, hgfg_fg). Main groups have empty hgfg_fg.
 */
@Entity
@Table(name = "w_hgfg")
@IdClass(SubGroupId.class)
@Data
public class MainGroup {
    /**
     * Main group code (HG).
     */
    @Id
    @Column(name = "hgfg_hg")
    private String mainGroupCode;

    /**
     * Function group code (FG). Empty string for main group entries.
     */
    @Id
    @Column(name = "hgfg_fg")
    private String functionGroupCode;

    /**
     * Text code used to resolve the group name.
     */
    @Column(name = "hgfg_textcode")
    private String textCode;

    /**
     * Graphic identifier for the group icon.
     */
    @Column(name = "hgfg_grafikid")
    private String graphicId;

    /**
     * Value line flag indicating special catalog grouping.
     */
    @Column(name = "hgfg_ist_valueline")
    private String valueLineFlag;
}
