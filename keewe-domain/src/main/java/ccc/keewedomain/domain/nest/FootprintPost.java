package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.nest.enums.Visibility;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@DiscriminatorValue("FOOTPRINT")
public class FootprintPost extends Post {
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;
}
