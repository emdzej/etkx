package pl.etkx.dal.dto.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtkTextCommentDto {
    private String kommId;
    private String text;
    private Integer kommPos;
}
