package pl.emdzej.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesMarketingTextDto {
    private Integer productId;
    private Integer marketId;
    private String languageIso;
    private String languageRegionIso;
    private String type;
    private String text;
    private String textStart;
    private Integer textLength;
}
