package pl.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a line item in a diagram panel (Bildtafelzeile).
 * Maps to the w_btzeilen table linking parts to diagrams.
 * 
 * Each line shows a part at a specific position in the diagram.
 */
@Entity
@Table(name = "w_btzeilen")
@IdClass(DiagramLineId.class)
@Data
public class DiagramLine {
    /**
     * Diagram panel number (FK to w_bildtaf).
     */
    @Id
    @Column(name = "btzeilen_btnr")
    private String diagramNumber;

    /**
     * Line position within diagram.
     */
    @Id
    @Column(name = "btzeilen_pos")
    private Integer position;

    /**
     * Main group.
     */
    @Column(name = "btzeilen_hg")
    private String mainGroup;

    /**
     * Position number shown in diagram image.
     */
    @Column(name = "btzeilen_bildposnr")
    private String imagePosition;

    /**
     * Part number (FK to w_teil).
     */
    @Column(name = "btzeilen_sachnr")
    private String partNumber;

    /**
     * Category code.
     */
    @Column(name = "btzeilen_kat")
    private String category;

    /**
     * Automatic transmission indicator.
     */
    @Column(name = "btzeilen_automatik")
    private String automaticFlag;

    /**
     * Steering indicator (L/R).
     */
    @Column(name = "btzeilen_lenkg")
    private String steering;

    /**
     * Start date/usage (Einsatz).
     */
    @Column(name = "btzeilen_eins")
    private Integer startUsage;

    /**
     * End date/usage (Auslauf).
     */
    @Column(name = "btzeilen_auslf")
    private Integer endUsage;

    /**
     * Condition code.
     */
    @Column(name = "btzeilen_bedkez")
    private String conditionCode;

    /**
     * Rule number.
     */
    @Column(name = "btzeilen_regelnr")
    private Integer ruleNumber;

    /**
     * Comment diagram reference.
     */
    @Column(name = "btzeilen_kommbt")
    private Integer commentDiagram;

    /**
     * Pre-comment reference.
     */
    @Column(name = "btzeilen_kommvor")
    private Integer preComment;

    /**
     * Post-comment reference.
     */
    @Column(name = "btzeilen_kommnach")
    private Integer postComment;

    /**
     * Group ID.
     */
    @Column(name = "btzeilen_gruppeid")
    private Integer groupId;

    /**
     * Block number.
     */
    @Column(name = "btzeilen_blocknr")
    private Integer blockNumber;

    /**
     * PG condition code.
     */
    @Column(name = "btzeilen_bedkez_pg")
    private String pgConditionCode;
}
