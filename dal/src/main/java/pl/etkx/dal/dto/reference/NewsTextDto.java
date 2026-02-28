package pl.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsTextDto {
    private String text;
    private String isAktiviert;
    private String isStandard;
}
