package pl.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesDiagramMarketingDto {
    private String diagramNumber;
    private Integer productId;
    private Integer marketId;
}
