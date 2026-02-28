package pl.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardPartNumberGraphicDto {
    private String nummer;
    private String art;
    private String grafikId;
    private Integer pos;
    private String ts;
}
