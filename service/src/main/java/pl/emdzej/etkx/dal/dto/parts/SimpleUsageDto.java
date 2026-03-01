package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleUsageDto {
    private String baureihe;
    private String karosserie;
    private String modell;
}
