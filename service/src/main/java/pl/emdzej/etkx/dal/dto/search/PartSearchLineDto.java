package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartSearchLineDto {
    private String hauptgruppe;
    private String untergruppe;
    private String sachnummer;
    private String benennung;
    private String zusatz;
    private String benennungKommentar;
    private String btZeilenAlter;
    private Integer pos;
    private String btNummer;
    private String teilDiebstahlrelevant;
}
