package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents localized text content by language and region.
 * Maps to the w_ben_gk table containing multilingual text lookups.
 * 
 * Used to retrieve part names, descriptions, and labels in different languages.
 */
@Entity
@Table(name = "w_ben_gk")
@IdClass(LocalizedTextId.class)
@Data
public class LocalizedText {
    /**
     * Text code - links to textcode fields in other tables.
     */
    @Id
    @Column(name = "ben_textcode")
    private Integer textCode;

    /**
     * Language ISO code (e.g., 'de', 'en', 'cs').
     */
    @Id
    @Column(name = "ben_iso")
    private String languageIso;

    /**
     * Regional ISO code for regional variations.
     */
    @Id
    @Column(name = "ben_regiso")
    private String regionIso;

    /**
     * Localized text content.
     */
    @Column(name = "ben_text")
    private String text;
}
