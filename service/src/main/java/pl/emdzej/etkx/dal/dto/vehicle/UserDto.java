package pl.emdzej.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String nutzerId;
    private String nutzerName;
    private String passwort;
    private String defaultFiliale;
    private String bearbeiterNummer;
}
