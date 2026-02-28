package pl.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetIndividualPartDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String benennung;
    private String zusatz;
    private String menge;
    private String istBeziehbar;
    private String vorhandenSi;
    private String benKommentarId;
    private String reach;
    private String aspg;
    private String teilDiebstahlrelevant;
    private String tc;
    private String istEba;
    private Integer pos;
}
