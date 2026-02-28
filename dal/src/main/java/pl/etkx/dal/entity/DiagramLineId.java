package pl.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite identifier for DiagramLine.
 */
@Data
public class DiagramLineId implements Serializable {
    /**
     * Diagram number (Bildtafel number).
     */
    private String diagramNumber;

    /**
     * Position within the diagram.
     */
    private String position;

    /**
     * Part number (Sachnummer).
     */
    private String partNumber;
}
