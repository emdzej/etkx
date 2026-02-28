package pl.emdzej.etkx.dal.dto.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketDto {
    private String code;
    private String countryCode;
    private String isoCode;
    private String name;
}
