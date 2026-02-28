package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;

/**
 * Represents a main group (HG) in the catalog hierarchy.
 * Maps to the w_hgfg table with group metadata used for navigation.
 */
@Entity
@Table(name = "w_hgfg")
@Data
public class MainGroup {
    /**
     * Main group code (HG).
     */
    @Id
    @Column(name = "hgfg_hg")
    private String mainGroupCode;

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

    /**
     * Sub-groups belonging to this main group.
     */
    @OneToMany(mappedBy = "mainGroup")
    private Set<SubGroup> subGroups;
}
