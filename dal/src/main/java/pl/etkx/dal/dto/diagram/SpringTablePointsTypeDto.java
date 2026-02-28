package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpringTablePointsTypeDto {
    private String ftId;
    private String typ;
    private String grundpunkteVaLinks;
    private String grundpunkteVaRechts;
    private String grundpunkteHaLinks;
    private String grundpunkteHaRechts;
    private String sftidIstAspg;
}
