package pl.etkx.dal.dto.accessories;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesMarketingInfoDto {
    private List<AccessoriesDiagramMarketingDto> diagramMarketing;
    private List<AccessoriesVariantMarketingDto> variantMarketing;
    private List<AccessoriesMarketingProductDto> products;
    private List<AccessoriesMarketingTextDto> texts;
    private List<AccessoriesMarketingKeywordDto> keywords;
    private List<AccessoriesMarketingGraphicDto> graphics;
    private List<AccessoriesMarketingRelatedDto> relatedProducts;
}
