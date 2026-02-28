package pl.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartReplacementDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String sachnummerAlt;
    private String hgAlt;
    private String ugAlt;
    private String at;
    private String benennung;
    private String zusatz;
    private String si;
    private String lzb;
    private String pi;
    private String benKommentarId;
    private String reach;
    private String aspg;
    private String tc;
    private String teilDiebstahlrelevant;
}
