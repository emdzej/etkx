package pl.emdzej.etkx.dal.dto.accessories;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesPriceDto {
    private String partNumber;
    private String companyId;
    private BigDecimal price;
    private String discountCode;
    private BigDecimal vatRate;
}
