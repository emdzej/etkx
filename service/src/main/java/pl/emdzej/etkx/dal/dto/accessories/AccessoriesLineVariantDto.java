package pl.emdzej.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesLineVariantDto {
    private String diagramNumber;
    private Integer position;
    private Integer variantId;
}
