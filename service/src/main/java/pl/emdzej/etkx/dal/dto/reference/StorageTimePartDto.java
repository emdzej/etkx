package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageTimePartDto {
    private String hg;
    private String ug;
    private String sachnr;
    private String benennung;
    private String zusatz;
    private String si;
    private String pi;
    private String benKommentarId;
    private String reach;
    private String aspg;
    private String tc;
    private String teilDiebstahlrelevant;
}
