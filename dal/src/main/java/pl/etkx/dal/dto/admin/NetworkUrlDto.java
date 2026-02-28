package pl.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NetworkUrlDto {
    private String asapUrl;
    private String asapTunnel;
    private String centralUrl;
    private String igdomBasicsUrl;
    private String igdomOptionsUrl;
}
