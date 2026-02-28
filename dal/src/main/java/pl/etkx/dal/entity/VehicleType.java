package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a vehicle type definition used to identify vehicles by technical configuration.
 * Maps to the w_fztyp table containing type keys and descriptive attributes.
 */
@Entity
@Table(name = "w_fztyp")
@Data
public class VehicleType {
    /**
     * Model/series/production identifier (MOSP ID).
     */
    @Id
    @Column(name = "fztyp_mospid")
    private Integer mospId;

    /**
     * Type key (Typschluessel).
     */
    @Column(name = "fztyp_typschl")
    private String typeKey;

    /**
     * Sales area (Verkaufsbereich).
     */
    @Column(name = "fztyp_vbereich")
    private String salesArea;

    /**
     * Catalytic converter indicator.
     */
    @Column(name = "fztyp_katalysator")
    private String catalyticConverter;

    /**
     * Visibility protection indicator.
     */
    @Column(name = "fztyp_sichtschutz")
    private String visibilityProtection;

    /**
     * Steering type (e.g., LHD/RHD).
     */
    @Column(name = "fztyp_lenkung")
    private String steering;

    /**
     * Transmission type.
     */
    @Column(name = "fztyp_getriebe")
    private String transmission;

    /**
     * Model series code (Baureihe).
     */
    @Column(name = "fztyp_baureihe")
    private String modelSeries;

    /**
     * Catalog edition identifier.
     */
    @Column(name = "fztyp_ktlgausf")
    private String catalogEdition;

    /**
     * Sales designation.
     */
    @Column(name = "fztyp_vbez")
    private String salesDesignation;

    /**
     * Extended sales designation.
     */
    @Column(name = "fztyp_erwvbez")
    private String extendedSalesDesignation;

    /**
     * Engine code.
     */
    @Column(name = "fztyp_motor")
    private String engine;

    /**
     * Body type (Karosserie).
     */
    @Column(name = "fztyp_karosserie")
    private String bodyType;

    /**
     * Usage indicator.
     */
    @Column(name = "fztyp_einsatz")
    private Integer usage;
}
