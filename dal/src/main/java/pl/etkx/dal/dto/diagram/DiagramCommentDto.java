package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramCommentDto {
    private String kommId;
    private String text;
    private String code;
    private String vz;
    private String darstellung;
    private String tiefe;
    private String pos;
}
