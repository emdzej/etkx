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
public class DiagramInfoDto {
    private DiagramTitleDto title;
    private List<DiagramCommentDto> comments;
}
