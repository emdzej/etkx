package pl.emdzej.etkx.dal.dto.parts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleReplacementDto {
    private String sachnummer;
    private String sachnummerAlt;
    private String benennung;
    private String zusatz;
    private String ersatzKez;
}
