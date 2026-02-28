package pl.emdzej.etkx.dal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents body type lookup values from w_bauart.
 */
@Entity
@Table(name = "w_bauart")
@Data
public class BodyType {
    @Id
    @Column(name = "bauart_bauart")
    private String code;

    @Column(name = "bauart_textcode")
    private Integer textCode;
}
