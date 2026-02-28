package pl.etkx.dal.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaureiheDto {
    private String baureihe;
    private String extBaureihe;
    private String pos;
}
