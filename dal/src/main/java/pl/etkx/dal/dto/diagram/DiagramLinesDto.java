package pl.etkx.dal.dto.diagram;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramLinesDto {
    private List<DiagramLineFzgDto> vehicleLines;
    private List<DiagramLineUgbDto> ugbLines;
    private List<DiagramCpLineDto> cpLines;
}
