package ccc.keewedomain.persistence.domain.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static lombok.AccessLevel.PROTECTED;

@Embeddable
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Interest {

    @Column(name = "interest_name", nullable = false, length = 8)
    private String name;

    public static Interest of(String name) {
        Interest interest = new Interest();
        interest.name = name;
        return interest;
    }
}
