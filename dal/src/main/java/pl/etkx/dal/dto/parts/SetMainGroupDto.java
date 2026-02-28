package pl.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetMainGroupDto {
    private String hg;
    private String benennung;
}
