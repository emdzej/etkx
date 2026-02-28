package pl.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsageDesignationLineDto {
    private String hauptgruppe;
    private String untergruppe;
    private String sachnummer;
    private String benennung;
    private String zusatz;
    private String benennungKommentar;
    private String teilDiebstahlrelevant;
}
