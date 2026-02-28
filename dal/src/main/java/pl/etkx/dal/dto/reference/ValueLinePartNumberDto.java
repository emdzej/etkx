package pl.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueLinePartNumberDto {
    private String hauptgruppe;
    private String untergruppe;
    private String sachnummer;
    private String benennung;
    private String zusatz;
    private String kommentar;
    private String teilDiebstahlrelevant;
}
