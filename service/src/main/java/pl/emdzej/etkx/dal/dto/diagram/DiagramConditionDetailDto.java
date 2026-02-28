package pl.emdzej.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramConditionDetailDto {
    private String gesamttermVz;
    private String gesamtterm;
    private String og;
    private String vArt;
    private String fZeile;
    private String elementVz;
    private String elementId;
    private String pos;
}
