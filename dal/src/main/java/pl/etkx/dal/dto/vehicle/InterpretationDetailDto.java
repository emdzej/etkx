package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InterpretationDetailDto {
    private String hg;
    private String ug;
    private String sachnummer;
    private String benennung;
    private String entfallDatum;
    private String bestellbar;
    private String marke;
    private String zweigNr;
    private String strukturNr;
}
