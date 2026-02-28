package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a localized text entry used for part names and other labels.
 * Maps to the w_publben table containing multilingual text values.
 */
@Entity
@Table(name = "w_publben")
@Data
@IdClass(PartTextId.class)
public class PartText {
    /**
     * Text code linking to language keys.
     */
    @Id
    @Column(name = "publben_textcode")
    private String textCode;

    /**
     * Language ISO code.
     */
    @Id
    @Column(name = "publben_iso")
    private String languageIso;

    /**
     * Region ISO code.
     */
    @Id
    @Column(name = "publben_regiso")
    private String regionIso;

    /**
     * Text value in the specified language.
     */
    @Column(name = "publben_text")
    private String text;

    /**
     * Text category/type (e.g., K=body, L=steering).
     */
    @Column(name = "publben_art")
    private String textType;

    /**
     * Designation/code that the text describes.
     */
    @Column(name = "publben_bezeichnung")
    private String designation;
}
