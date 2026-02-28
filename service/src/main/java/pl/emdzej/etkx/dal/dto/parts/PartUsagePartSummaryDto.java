package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsagePartSummaryDto {
    private String hg;
    private String ug;
    private String marke;
    private String produktart;
    private String ben;
    private String teilDiebstahlrelevant;
}
