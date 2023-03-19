package ccc.keewedomain.persistence.domain.insight.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    SPAM("스팸"),
    INAPPROPRIATE_CONTENT("부적절한 내용(혐오/음란)"),
    ABUSE("과도한 비속어/욕설"),
    IMPERSONATION("사칭/사기 의심"),
    OTHERS("기타 신고 사유")
    ;

    private String reason;
}
