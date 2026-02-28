package pl.emdzej.etkx.dal.dto.diagram;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartVisualizationResponseDto {
    private List<PartVisualizationDto> vehicleVisualizations;
    private List<PartVisualizationDto> ugbVisualizations;
}
