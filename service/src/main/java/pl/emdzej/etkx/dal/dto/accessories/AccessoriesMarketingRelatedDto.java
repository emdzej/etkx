package pl.emdzej.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesMarketingRelatedDto {
    private Integer productId;
    private Integer marketId;
    private Integer relatedProductId;
}
