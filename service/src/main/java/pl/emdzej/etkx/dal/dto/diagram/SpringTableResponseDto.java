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
public class SpringTableResponseDto {
    private List<SpringTableSalaDto> salas;
    private List<SpringTablePointsTypeDto> pointsByType;
    private List<SpringTablePointsSalaDto> pointsBySala;
    private List<SpringTableSpringDto> leftSprings;
    private List<SpringTableSpringDto> rightSprings;
    private List<SpringTableAspgKitDto> aspgKits;
}
