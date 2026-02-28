package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsagePlateDto {
    private String bildtafelNr;
    private String bildtafelArt;
    private String benennung;
    private String kommentar;
    private String cpVorhanden;
    private String bedingungKz;
    private Integer pos;
    private String marktIso;
}
