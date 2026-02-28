package pl.emdzej.etkx.dal.dto.diagram;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpringTableAspgKitDto {
    private String teilenummer;
    private String btnr;
    private String mospId;
    private String achse;
    private String teilDiebstahlrelevant;
}
