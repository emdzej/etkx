package pl.emdzej.etkx.dal.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagramSummaryDto {
    private String btnr;
    private Integer grafikId;
    private String name;
}
