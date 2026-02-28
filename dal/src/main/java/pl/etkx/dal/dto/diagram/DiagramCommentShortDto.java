package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramCommentShortDto {
    private String kommId;
    private String text;
    private String pos;
}
