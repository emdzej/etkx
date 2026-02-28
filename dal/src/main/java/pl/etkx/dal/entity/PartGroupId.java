package pl.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite key for PartGroup entity.
 */
@Data
public class PartGroupId implements Serializable {
    private String mainGroup;
    private String subGroup;
}
