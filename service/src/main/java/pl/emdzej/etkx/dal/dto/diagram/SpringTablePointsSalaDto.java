package pl.emdzej.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpringTablePointsSalaDto {
    private String ftId;
    private String salaId;
    private String punkteVaLinks;
    private String punkteVaRechts;
    private String punkteHaLinks;
    private String punkteHaRechts;
}
