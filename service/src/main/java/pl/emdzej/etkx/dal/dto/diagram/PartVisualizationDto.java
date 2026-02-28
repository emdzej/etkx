package pl.emdzej.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartVisualizationDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String teilebenennung;
    private String teilebenennungZusatz;
    private String bildtafelNummer;
    private String bildtafelUeberschrift;
    private String bildnummer;
    private String grafikId;
    private String grafikFormat;
    private String grafikTimestamp;
}
