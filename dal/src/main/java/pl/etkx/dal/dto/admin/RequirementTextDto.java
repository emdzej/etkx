package pl.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementTextDto {
    private String elementId;
    private String benennung;
    private String eGruppenId;
    private String exklusiv;
    private String eGruppenPosition;
}
