package pl.etkx.dal.dto.accessories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessoriesLineItemDto {
    private String diagramNumber;
    private Integer position;
    private Integer groupPosition;
    private String imagePosition;
    private String type;
    private Integer administrationFlag;
    private String partNumber;
    private String elementType;
    private String bundling;
    private String quantityMin;
    private String quantityMax;
    private String category;
    private String automatic;
    private String steering;
    private Integer validFrom;
    private Integer validTo;
    private String conditionCode;
    private Integer ruleNumber;
    private Integer groupId;
}
