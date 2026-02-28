package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleIdentificationDto {
    private String modellspalte;
    private String typ;
    private String werk;
    private String marke;
    private String produktart;
    private String katalogumfang;
    private String baureihe;
    private String extBaureihe;
    private String bauart;
    private String extBauart;
    private String karosserie;
    private String extKarosserie;
    private String motor;
    private String modell;
    private String region;
    private String lenkung;
    private String getriebe;
    private String produktionsdatum;
    private String sichtschutz;
    private String einsatz;
}
