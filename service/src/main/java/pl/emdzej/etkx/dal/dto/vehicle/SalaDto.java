package pl.emdzej.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaDto {
    private String elementId;
    private String code;
    private String benennung;
    private String eGruppenId;
    private String sicher;
    private String saz;
    private String hasBedText;
    private String showBedText;
    private String exklusiv;
    private String eGruppenPosition;
    private String primaNr;
}
