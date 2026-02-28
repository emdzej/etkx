package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramReferenceDto {
    private String bildtafelnummer;
    private String ueberschrift;
    private String text;
    private String pos;
}
