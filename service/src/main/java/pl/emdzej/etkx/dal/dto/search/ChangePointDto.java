package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePointDto {
    private Integer pos;
    private String typ;
    private String werk;
    private String art;
    private String datum;
    private String vin;
    private String vinProddatum;
    private String vinMin;
    private String vinMax;
    private String artNummer;
    private String nummer;
    private String cpAlter;
}
