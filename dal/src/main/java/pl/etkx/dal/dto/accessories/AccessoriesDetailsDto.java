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
public class AccessoriesDetailsDto {
    private AccessoriesDiagramDto diagram;
    private List<AccessoriesLineItemDto> lineItems;
}
