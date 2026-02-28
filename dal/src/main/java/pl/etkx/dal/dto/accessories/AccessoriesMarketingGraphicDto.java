package pl.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesMarketingGraphicDto {
    private Integer productId;
    private Integer marketId;
    private Integer graphicId;
    private Integer position;
    private String type;
}
