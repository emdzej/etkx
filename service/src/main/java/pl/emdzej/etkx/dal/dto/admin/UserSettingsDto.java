package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
    private String userMarke;
    private String userProduktart;
    private String userLenkung;
    private String userKatalogumfang;
    private String userIso;
    private String userRegiso;
    private String userExpandBnb;
    private String userShortSearchpath;
    private String userRequestSaz;
    private String userShowProddate;
    private String userSuchraum;
    private String userShowPreise;
    private String userShowTipps;
    private String userPrimaermarktId;
    private String userTablestretch;
    private String userFontsize;
    private String userDftVerbaumenge;
    private String userAufbewahrung;
}
