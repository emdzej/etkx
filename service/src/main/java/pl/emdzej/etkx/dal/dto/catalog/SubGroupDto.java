package pl.emdzej.etkx.dal.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubGroupDto {
    private String hg;
    private String fg;
    private String name;
    private Integer thumbnailId;
    private String btnr;
}
