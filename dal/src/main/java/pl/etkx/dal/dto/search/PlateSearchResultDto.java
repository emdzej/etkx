package pl.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlateSearchResultDto {
    private String bildtafelHg;
    private String bildtafelNr;
    private String bildtafelArt;
    private String benennung;
    private Integer pos;
    private String kommentar;
    private String cpVorhanden;
    private String bedingungKz;
    private String marktIso;
}
