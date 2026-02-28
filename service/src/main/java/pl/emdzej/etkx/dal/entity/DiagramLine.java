package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

/**
 * Represents a line item within a diagram panel, linking a part to its position.
 * Maps to the w_btzeilen table containing diagram parts lists.
 */
@Entity
@Table(name = "w_btzeilen")
@Data
@IdClass(DiagramLineId.class)
public class DiagramLine {
    /**
     * Diagram number (Bildtafel number).
     */
    @Id
    @Column(name = "btzeilen_btnr")
    private String diagramNumber;

    /**
     * Position within the diagram.
     */
    @Id
    @Column(name = "btzeilen_pos")
    private String position;

    /**
     * Part number (Sachnummer).
     */
    @Id
    @Column(name = "btzeilen_sachnr")
    private String partNumber;

    /**
     * Quantity of the part in the diagram.
     */
    @Column(name = "btzeilen_menge")
    private BigDecimal quantity;

    /**
     * Condition code for applicability.
     */
    @Column(name = "btzeilen_bedkez")
    private String conditionCode;

    /**
     * Text code used for line-specific annotation.
     */
    @Column(name = "btzeilen_textc")
    private String textCode;

    /**
     * Change point identifier.
     */
    @Column(name = "btzeilen_cp")
    private String changePoint;

    /**
     * Diagram panel this line belongs to.
     */
    @ManyToOne
    @JoinColumn(name = "btzeilen_btnr", referencedColumnName = "bildtaf_btnr", insertable = false, updatable = false)
    private DiagramPanel diagramPanel;
}
