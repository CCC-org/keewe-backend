package ccc.keeweapi.dto.nest;

import ccc.keeweapi.validator.annotations.Enum;
import ccc.keeweapi.validator.annotations.FieldsValueMismatch;
import ccc.keewedomain.domain.nest.enums.Visibility;
import lombok.Data;

@Data
@FieldsValueMismatch(from = "writerId", to = "profileId", message = "자신의 둥지에 발자국을 등록할 수 없습니다.")
public class FootprintPostCreateRequest extends PostCreateRequest {
    private Long writerId;
    @Enum(enumClass = Visibility.class)
    private String visibility;
}
