package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlateMasterDto {
    private String hg;
    private String fg;
    private String bildtafelNr;
    private String bildtafelArt;
    private String benennung;
    private String bedKuerzel;
    private String vorhandenCp;
    private String lkz;
    private String grafikId;
    private String modStamp;
    private String zubBtnr;
}
