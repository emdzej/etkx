package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueLinePlateDto {
    private String bildtafelNr;
    private String benennung;
    private String modell;
    private String karosserie;
    private String karosserieId;
    private String bauart;
    private String region;
    private String kommentar;
    private String cpVorhanden;
    private String bedingungKz;
    private Integer pos;
}
