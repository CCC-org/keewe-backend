package ccc.keewedomain.persistence.domain.insight.id;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BookmarkId implements Serializable {
    private Long user;
    private Long insight;
}

