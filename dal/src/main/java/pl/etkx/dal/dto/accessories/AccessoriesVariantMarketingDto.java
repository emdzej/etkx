package pl.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesVariantMarketingDto {
    private String diagramNumber;
    private Integer variantId;
    private Integer productId;
    private Integer marketId;
}
