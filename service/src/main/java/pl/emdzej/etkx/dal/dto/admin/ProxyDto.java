package pl.emdzej.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProxyDto {
    private String proxyName;
    private String port;
    private String userName;
    private String passwort;
    private String realm;
    private String ntHost;
    private String ntDomain;
}
