package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramLineUgbDto {
    private String bildnummer;
    private String teilHg;
    private String teilUg;
    private String teilSachnummer;
    private String teilBenennung;
    private String teilZusatz;
    private String teilEntfall;
    private String teilKommentarId;
    private String teilKommentar;
    private String teilKommPi;
    private String teilSi;
    private String teilReach;
    private String teilAspg;
    private String teilStecker;
    private String teilDiebstahlrelevant;
    private String siDokArt;
    private String teilTc;
    private String teilTcProdDatRelevant;
    private String mmg;
    private String emg;
    private String einsatz;
    private String auslauf;
    private String kommBt;
    private String kommVor;
    private String kommNach;
    private String satzSachnummer;
    private String gruppeId;
    private String blockNr;
    private String bnbBenText;
    private String pos;
}
