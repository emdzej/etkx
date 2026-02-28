package pl.emdzej.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesInstallationInfoDto {
    private String diagramNumber;
    private String type;
    private String series;
    private String installationId;
    private Integer installationInfoId;
    private String complexity;
    private String readingType;
    private String mechanical;
    private String electrical;
    private String programming;
    private String paint;
    private String total;
}
