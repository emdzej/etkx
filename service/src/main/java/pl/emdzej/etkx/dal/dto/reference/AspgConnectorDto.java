package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AspgConnectorDto {
    private String hauptgr;
    private String untergrup;
    private String sachnrPg;
    private String benText;
    private String vmenge;
    private String benennZus;
    private String technik;
    private String entfallKez;
    private String teilDiebstahlrelevant;
}
