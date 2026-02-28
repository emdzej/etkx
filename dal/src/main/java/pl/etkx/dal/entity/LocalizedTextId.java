package pl.etkx.dal.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * Composite key for LocalizedText entity.
 */
@Data
public class LocalizedTextId implements Serializable {
    private Integer textCode;
    private String languageIso;
    private String regionIso;
}
