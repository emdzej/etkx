package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
    private String userId;
    private String passwort;
    private String defaultFiliale;
    private String bearbeiterNummer;
    private String filialBezeichnung;
    private String iso;
    private String regIso;
}
