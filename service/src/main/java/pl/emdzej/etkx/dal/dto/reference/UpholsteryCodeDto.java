package pl.emdzej.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpholsteryCodeDto {
    private String art;
    private String code;
    private String benennung;
    private String pcode;
    private String gueltigVon;
    private String gueltigBis;
    private Integer pos;
}
