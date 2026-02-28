package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsageModelColumnDto {
    private String menge;
    private String btnr;
    private String btUeberschrift;
    private String modell;
    private String karosserie;
    private String karosserieId;
    private String bauart;
    private String region;
    private String kommentar;
    private String cpVorhanden;
    private String bedingungKz;
    private String marktIso;
}
