package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

/**
 * Represents a BMW part master record.
 * Maps to the w_teil table containing core part catalog data.
 */
@Entity
@Table(name = "w_teil")
@Data
public class Part {
    /**
     * Unique part number (Sachnummer).
     */
    @Id
    @Column(name = "teil_sachnr")
    private String partNumber;

    /**
     * Part type/category code.
     */
    @Column(name = "teil_art")
    private String partType;

    /**
     * Brand identifier (e.g., BMW, MINI, RR).
     */
    @Column(name = "teil_marke")
    private String brand;

    /**
     * Main group (HG) identifier.
     */
    @Column(name = "teil_hauptgr")
    private String mainGroup;

    /**
     * Sub group (UG) identifier.
     */
    @Column(name = "teil_untergrup")
    private String subGroup;

    /**
     * Unit of measure for the part.
     */
    @Column(name = "teil_mengeeinh")
    private String unitOfMeasure;

    /**
     * Part weight in catalog units.
     */
    @Column(name = "teil_teile_gew")
    private BigDecimal weight;

    /**
     * Flag indicating whether the part is orderable.
     */
    @Column(name = "teil_bestellbar")
    private String orderableFlag;

    /**
     * Date when the part was discontinued.
     */
    @Column(name = "teil_entfall_dat")
    private LocalDate discontinuationDate;

    /**
     * Discontinuation code.
     */
    @Column(name = "teil_entfall_kez")
    private String discontinuationCode;

    /**
     * Replacement part number (old part reference).
     */
    @Column(name = "teil_austausch_alt")
    private String replacementPartNumber;

    /**
     * Theft-relevant flag.
     */
    @Column(name = "teil_ist_diebstahlrelevant")
    private String theftRelevantFlag;

    /**
     * REACH compliance flag.
     */
    @Column(name = "teil_ist_reach")
    private String reachCompliantFlag;

    /**
     * Connector part flag.
     */
    @Column(name = "teil_ist_stecker")
    private String connectorFlag;

    /**
     * Value line flag.
     */
    @Column(name = "teil_ist_valueline")
    private String valueLineFlag;

    /**
     * Standard number reference.
     */
    @Column(name = "teil_normnummer")
    private String standardNumber;

    /**
     * Product type code.
     */
    @Column(name = "teil_produktart")
    private String productType;

    /**
     * Product class code.
     */
    @Column(name = "teil_produktkl")
    private String productClass;

    /**
     * Country code (LKZ).
     */
    @Column(name = "teil_lkz")
    private String countryCode;

    /**
     * Availability code (LZB).
     */
    @Column(name = "teil_lzb")
    private String availabilityCode;

    /**
     * Text code for localized part names.
     */
    @Column(name = "teil_textcode")
    private String textCode;
}
