package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsageVehiclePartDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String sachnummerAlt;
    private String at;
    private String hgAlt;
    private String ugAlt;
    private String benennung;
    private String zusatz;
    private String si;
    private String lzb;
    private String pi;
    private String benKommentarId;
    private String reach;
    private String aspg;
    private String stecker;
    private String tc;
    private String teilDiebstahlrelevant;
}
