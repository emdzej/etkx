package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartUsageReductionPartDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String ben;
    private String zusatz;
}
