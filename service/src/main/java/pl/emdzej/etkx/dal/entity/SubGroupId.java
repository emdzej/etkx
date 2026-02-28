package pl.emdzej.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite identifier for SubGroup.
 */
@Data
public class SubGroupId implements Serializable {
    /**
     * Main group code (HG).
     */
    private String mainGroupCode;

    /**
     * Function group code (FG).
     */
    private String functionGroupCode;
}
