package pl.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FillQuantityDto {
    private String typ;
    private String getriebe;
    private String motor;
    private String fmGetriebe;
    private String fmMotor;
    private String fmHinterachse;
    private String fmKuehlmittelMitAc;
    private String fmKuehlmittelOhneAc;
    private String fmBremse;
    private String hinweis;
}
