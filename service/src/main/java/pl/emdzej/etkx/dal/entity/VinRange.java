package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a VIN range used to map chassis number intervals to vehicle types.
 * Maps to the w_fgstnr table with VIN boundaries and related identifiers.
 */
@Entity
@Table(name = "w_fgstnr")
@Data
public class VinRange {
    /**
     * Range start VIN (Anfang).
     */
    @Id
    @Column(name = "fgstnr_anf")
    private String startVin;

    /**
     * VIN range start (Von).
     */
    @Column(name = "fgstnr_von")
    private String fromVin;

    /**
     * VIN range end (Bis).
     */
    @Column(name = "fgstnr_bis")
    private String toVin;

    /**
     * Type key (Typschluessel).
     */
    @Column(name = "fgstnr_typschl")
    private String typeKey;

    /**
     * Production date identifier.
     */
    @Column(name = "fgstnr_prod")
    private Integer productionDate;

    /**
     * Model/series/production identifier (MOSP ID).
     */
    @Column(name = "fgstnr_mospid")
    private Integer mospId;

    /**
     * Production plant (Werk).
     */
    @Column(name = "fgstnr_werk")
    private String plant;
}
