package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a function group (FG) within a main group.
 * Maps to the w_hgfg table for hierarchical parts catalog structure.
 */
@Entity
@Table(name = "w_hgfg")
@Data
@IdClass(SubGroupId.class)
public class SubGroup {
    /**
     * Main group code (HG).
     */
    @Id
    @Column(name = "hgfg_hg")
    private String mainGroupCode;

    /**
     * Function group code (FG).
     */
    @Id
    @Column(name = "hgfg_fg")
    private String functionGroupCode;

    /**
     * Text code used to resolve the subgroup name.
     */
    @Column(name = "hgfg_textcode")
    private String textCode;

    /**
     * Graphic identifier for the subgroup icon.
     */
    @Column(name = "hgfg_grafikid")
    private String graphicId;

    /**
     * Value line flag indicating special catalog grouping.
     */
    @Column(name = "hgfg_ist_valueline")
    private String valueLineFlag;

    /**
     * Parent main group.
     */
    @ManyToOne
    @JoinColumn(name = "hgfg_hg", referencedColumnName = "hgfg_hg", insertable = false, updatable = false)
    private MainGroup mainGroup;
}
