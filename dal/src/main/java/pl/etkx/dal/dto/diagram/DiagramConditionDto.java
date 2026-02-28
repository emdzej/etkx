package pl.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramConditionDto {
    private String kuerzel;
    private String gesamttermVz;
    private String gesamtterm;
    private String og;
    private String vArt;
    private String fZeile;
    private String elementVz;
    private String elementId;
    private String pos;
}
