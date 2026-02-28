package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

/**
 * Represents a vehicle identification record.
 * Maps to the w_fzgident table used to resolve VIN-based vehicle attributes.
 */
@Entity
@Table(name = "w_fzgident")
@Data
public class Vehicle {
    /**
     * Vehicle identification number (VIN).
     */
    @Id
    @Column(name = "fzgident_vin")
    private String vin;

    /**
     * Model/series/production identifier (MOSP ID).
     */
    @Column(name = "fzgident_mospid")
    private String modelSeriesProductionId;

    /**
     * Type key (Typschluessel).
     */
    @Column(name = "fzgident_typschl")
    private String typeKey;

    /**
     * Model series (Baureihe).
     */
    @Column(name = "fzgident_baureihe")
    private String modelSeries;

    /**
     * Body construction type (Bauart).
     */
    @Column(name = "fzgident_bauart")
    private String bodyType;

    /**
     * Body code (Karosserie).
     */
    @Column(name = "fzgident_karosserie")
    private String bodyCode;

    /**
     * Steering type (LHD/RHD).
     */
    @Column(name = "fzgident_lenkung")
    private String steering;

    /**
     * Transmission type.
     */
    @Column(name = "fzgident_getriebe")
    private String transmission;

    /**
     * Engine code.
     */
    @Column(name = "fzgident_motor")
    private String engine;

    /**
     * Catalog region (Katalogumfang).
     */
    @Column(name = "fzgident_ktlgausf")
    private String catalogRegion;

    /**
     * Brand identifier.
     */
    @Column(name = "fzgident_marke")
    private String brand;

    /**
     * Product type (e.g., P=passenger).
     */
    @Column(name = "fzgident_produktart")
    private String productType;

    /**
     * Production date.
     */
    @Column(name = "fzgident_prod")
    private LocalDate productionDate;
}
