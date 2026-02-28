package pl.etkx.dal.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupThumbnailDto {
    private String hauptgruppe;
    private String funktionsgruppe;
    private String benennung;
    private String grafikId;
    private String modStamp;
    private byte[] grafik;
}
