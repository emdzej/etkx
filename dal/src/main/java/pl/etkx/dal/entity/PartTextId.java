package pl.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite key for PartText entity.
 */
@Data
public class PartTextId implements Serializable {
    private String type;
    private Integer textCode;
}
