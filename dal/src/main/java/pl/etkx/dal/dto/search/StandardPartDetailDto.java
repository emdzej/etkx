package pl.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardPartDetailDto {
    private String benennung;
    private String hg;
    private String ug;
    private String sachnummer;
    private String zusatz;
    private String normart;
    private String normnummer;
    private String produktart;
    private String marke;
    private String teilDiebstahlrelevant;
}
