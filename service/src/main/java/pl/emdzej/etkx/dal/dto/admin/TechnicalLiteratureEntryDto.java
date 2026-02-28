package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TechnicalLiteratureEntryDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String istEba;
    private String benennung;
    private String kommentar;
    private String zusatz;
    private String einsatz;
    private String auslauf;
    private String mam;
    private String teilDiebstahlrelevant;
}
