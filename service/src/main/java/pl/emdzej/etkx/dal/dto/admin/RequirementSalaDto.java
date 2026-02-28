package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementSalaDto {
    private String elementId;
    private String code;
    private String benennung;
    private String eGruppenId;
    private String exklusiv;
    private String saz;
    private String eGruppenPosition;
    private String primaNr;
}
