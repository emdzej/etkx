package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Represents a BMW part master record.
 * Maps to the w_teil table containing core part catalog data.
 * 
 * Note: Brand information is stored in separate table w_teil_marken
 * linked via part number (teilm_sachnr).
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
     * Main group (Hauptgruppe) identifier.
     */
    @Column(name = "teil_hauptgr")
    private String mainGroup;

    /**
     * Sub group (Untergruppe) identifier.
     */
    @Column(name = "teil_untergrup")
    private String subGroup;

    /**
     * Text code for localized part name lookup in w_ben_gk.
     */
    @Column(name = "teil_textcode")
    private Integer textCode;

    /**
     * Text code for comment lookup.
     */
    @Column(name = "teil_textcode_kom")
    private Integer commentTextCode;

    /**
     * Comment PI reference.
     */
    @Column(name = "teil_kom_pi")
    private Integer commentPi;

    /**
     * Name suffix (Benennungszusatz).
     */
    @Column(name = "teil_benennzus")
    private String nameSuffix;

    /**
     * Rounding indicator.
     */
    @Column(name = "teil_rundung")
    private String roundingIndicator;

    /**
     * Product class code.
     */
    @Column(name = "teil_produktkl")
    private String productClass;

    /**
     * Old part number reference (Altteil).
     */
    @Column(name = "teil_alt")
    private String oldPartNumber;

    /**
     * Old part exchangeable flag.
     */
    @Column(name = "teil_austausch_alt")
    private String oldPartExchangeable;

    /**
     * Replacement/successor part number (Tauschteil).
     */
    @Column(name = "teil_tausch")
    private String replacementPartNumber;

    /**
     * Part type/category code.
     */
    @Column(name = "teil_art")
    private String partType;

    /**
     * Unit of measure (Mengeneinheit).
     */
    @Column(name = "teil_mengeeinh")
    private String unitOfMeasure;

    /**
     * Usage indicator (Einsatz).
     */
    @Column(name = "teil_einsatz")
    private Integer usage;

    /**
     * Series usage indicator.
     */
    @Column(name = "teil_einsatz_serie")
    private Integer seriesUsage;

    /**
     * Discontinuation code.
     */
    @Column(name = "teil_entfall_kez")
    private String discontinuationCode;

    /**
     * Discontinuation date (as integer YYYYMMDD).
     */
    @Column(name = "teil_entfall_dat")
    private Integer discontinuationDate;

    /**
     * Part weight in kg.
     */
    @Column(name = "teil_teile_gew")
    private BigDecimal weight;

    /**
     * Country code (Laenderkennzeichen).
     */
    @Column(name = "teil_lkz")
    private String countryCode;

    /**
     * Product type.
     */
    @Column(name = "teil_produktart")
    private String productType;

    /**
     * Recycling code.
     */
    @Column(name = "teil_recycling_kez")
    private String recyclingCode;

    /**
     * Manufacturing note.
     */
    @Column(name = "teil_fertigungshinweis")
    private String manufacturingNote;

    /**
     * Norm/standard number (DIN).
     */
    @Column(name = "teil_normnummer")
    private String normNumber;

    /**
     * Technical indicator (TST).
     */
    @Column(name = "teil_technik")
    private String technicalIndicator;

    /**
     * Disposition indicator (TSD).
     */
    @Column(name = "teil_dispo")
    private String dispositionIndicator;

    /**
     * Orderable flag.
     */
    @Column(name = "teil_bestellbar")
    private String orderableFlag;

    /**
     * Installable flag.
     */
    @Column(name = "teil_verbaubar")
    private String installableFlag;

    /**
     * Demand part flag.
     */
    @Column(name = "teil_bedarfsteil")
    private String demandPartFlag;

    /**
     * Price indicator.
     */
    @Column(name = "teil_preis")
    private String priceIndicator;

    /**
     * ValueLine part flag.
     */
    @Column(name = "teil_ist_valueline")
    private String valueLineFlag;

    /**
     * REACH compliance flag.
     */
    @Column(name = "teil_ist_reach")
    private String reachFlag;

    /**
     * ASPG flag.
     */
    @Column(name = "teil_ist_aspg")
    private String aspgFlag;

    /**
     * Connector part flag.
     */
    @Column(name = "teil_ist_stecker")
    private String connectorFlag;

    /**
     * Theft-relevant flag.
     */
    @Column(name = "teil_ist_diebstahlrelevant")
    private String theftRelevantFlag;

    /**
     * Tire flag.
     */
    @Column(name = "teil_ist_reifen")
    private String tireFlag;
}
