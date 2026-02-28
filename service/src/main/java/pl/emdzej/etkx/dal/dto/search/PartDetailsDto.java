package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDetailsDto {
    private String hg;
    private String ug;
    private String benennung;
    private String zusatz;
    private String marke;
    private String teileart;
    private String produktKlasse;
    private String mam;
    private String mengeneinheit;
    private Integer vvm;
    private Integer lvm;
    private Integer bvm;
    private String fh;
    private String teilDiebstahlrelevant;
}
