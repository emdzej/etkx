package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValueLineSeriesDto {
    private String baureihe;
    private String extBaureihe;
    private Integer pos;
}
