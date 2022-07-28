package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keeweapi.validator.annotations.FieldsValueCompare;
import ccc.keewedomain.domain.nest.enums.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonTypeName("FOOTPRINT")
@FieldsValueCompare(from = "writerId", to = "profileId", match = false, message = "자신의 둥지에 발자국을 등록할 수 없습니다.")
public class FootprintPostCreateRequest extends PostCreateRequest {
    private Long writerId;
    @Enum(enumClass = Visibility.class)
    private String visibility;
}
