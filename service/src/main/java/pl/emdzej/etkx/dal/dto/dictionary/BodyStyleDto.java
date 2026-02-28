package pl.emdzej.etkx.dal.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyStyleDto {
    private String seriesCode;
    private String bodyCode;
    private Integer graphicId;
}
