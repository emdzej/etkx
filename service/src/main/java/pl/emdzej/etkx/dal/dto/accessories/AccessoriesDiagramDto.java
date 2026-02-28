package pl.emdzej.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesDiagramDto {
    private String diagramNumber;
    private Integer saleId;
    private String conditionCode;
    private String ipacFlag;
    private String sa2Flag;
    private String sa3Flag;
    private Integer additionalCommentId;
}
