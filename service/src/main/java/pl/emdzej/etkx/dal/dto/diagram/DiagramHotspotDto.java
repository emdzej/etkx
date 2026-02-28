package pl.emdzej.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramHotspotDto {
    private String bildnummer;
    private String topLeftX;
    private String topLeftY;
    private String bottomRightX;
    private String bottomRightY;
}
