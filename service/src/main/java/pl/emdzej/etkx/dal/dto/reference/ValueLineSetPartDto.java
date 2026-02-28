package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueLineSetPartDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String benennung;
    private String zusatz;
    private String vorhandenSi;
    private String benKommentarId;
    private String reach;
    private String aspg;
    private String tc;
    private String teilDiebstahlrelevant;
}
