package pl.emdzej.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfoDto {
    private String firmaId;
    private String firmaBezeichnung;
    private String filialeId;
    private String filialeBezeichnung;
    private String spracheIso;
    private String spracheRegIso;
}
