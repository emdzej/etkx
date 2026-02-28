package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BedConditionDto {
    private String elementId;
    private String code;
    private String benennung;
    private String eGruppenId;
    private String vorhandenInfo;
    private String exklusiv;
    private String bedingungsart;
    private String eGruppenPosition;
}
