package pl.emdzej.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlateGraphicSearchResultDto {
    private String bildtafelNr;
    private String bildtafelArt;
    private String benennung;
    private Integer pos;
    private String grafikId;
    private String bedingungKz;
    private byte[] grafik;
    private String modStamp;
    private String marktIso;
}
