package pl.etkx.dal.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMarketDto {
    private String userMarktId;
    private String name;
    private String kuerzel;
    private String lkz;
}
