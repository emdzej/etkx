package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents published name/designation mapping.
 * Maps to the w_publben table containing type→designation mappings.
 * 
 * Note: For localized texts by language, see LocalizedText (w_ben_gk).
 */
@Entity
@Table(name = "w_publben")
@IdClass(PartTextId.class)
@Data
public class PartText {
    /**
     * Type/category code (e.g., 'A' for accessories).
     */
    @Id
    @Column(name = "publben_art")
    private String type;

    /**
     * Text code reference.
     */
    @Id
    @Column(name = "publben_textcode")
    private Integer textCode;

    /**
     * Designation/name.
     */
    @Column(name = "publben_bezeichnung")
    private String designation;
}
