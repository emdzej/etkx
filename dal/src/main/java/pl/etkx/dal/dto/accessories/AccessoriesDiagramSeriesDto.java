package pl.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesDiagramSeriesDto {
    private String diagramNumber;
    private String series;
    private String catalogVariant;
}
