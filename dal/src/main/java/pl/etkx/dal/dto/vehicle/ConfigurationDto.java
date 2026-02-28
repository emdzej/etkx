package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {
    private String firma;
    private String zusatz;
    private String strasse;
    private String plz;
    private String ort;
    private String telefon;
    private String pkw;
    private String motorrad;
    private String mwstNiedrig;
    private String mwstHoch;
    private String mwstAltteile;
    private String mwst3;
    private String mwst4;
    private String rechnungsNr;
    private String mailserver;
    private String barverkaufsNr;
    private String auftragsNr;
    private String kundenNr;
    private String verwenden;
    private String abwicklung;
    private String bestandfiliale;
    private String datenabgleich;
}
