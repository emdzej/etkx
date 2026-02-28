package pl.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtkTextEntryDto {
    private String elemId;
    private String hg;
    private String fg;
    private String produktart;
    private String kommId;
}
