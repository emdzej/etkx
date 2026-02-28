package pl.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsageSeriesDto {
    private String baureihe;
    private String extBaureihe;
    private Integer pos;
}
