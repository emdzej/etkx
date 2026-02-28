package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardPartGroupGraphicDto {
    private String nummer;
    private String grafikId;
    private Integer pos;
    private String ts;
}
