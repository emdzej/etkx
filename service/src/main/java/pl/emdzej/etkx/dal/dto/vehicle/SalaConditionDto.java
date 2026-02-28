package pl.emdzej.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalaConditionDto {
    private String elementId;
    private String code;
    private String benennung;
    private String eGruppenId;
    private String vorhandenInfo;
    private String exklusiv;
    private String sicher;
    private String saz;
    private String eGruppenPosition;
    private String primaNr;
}
