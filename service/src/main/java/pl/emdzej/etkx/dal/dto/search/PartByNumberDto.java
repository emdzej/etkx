package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartByNumberDto {
    private String sachnr;
    private String benennung;
    private String zusatz;
    private String hauptgr;
    private String untergrup;
}
