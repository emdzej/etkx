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
public class DiagramDetailsDto {
    private List<DiagramHotspotDto> hotspots;
    private List<DiagramCommentDto> vehicleComments;
    private List<DiagramCommentShortDto> ugbComments;
    private List<DiagramConditionDto> conditions;
    private List<DiagramOverConditionDto> overConditions;
    private List<DiagramReferenceDto> references;
    private List<DiagramYesNoTextDto> yesNoTexts;
}
