package pl.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite identifier for PartText.
 */
@Data
public class PartTextId implements Serializable {
    /**
     * Text code linking to generic language keys.
     */
    private String textCode;

    /**
     * Language ISO code.
     */
    private String languageIso;

    /**
     * Region ISO code.
     */
    private String regionIso;
}
