package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.nest.enums.Visibility;
import ccc.keewedomain.domain.user.Profile;
import ccc.keewedomain.dto.nest.FootprintPostDto;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
@DiscriminatorValue("FOOTPRINT")
public class FootprintPost extends Post {
    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    private Visibility visibility;

    protected FootprintPost(Nest nest, Profile writer, String content) {
        super(nest, writer, content);
    }

    public static FootprintPost of(Nest nest, Profile writer, String content, Visibility visibility) {
        FootprintPost post = new FootprintPost(nest, writer, content);
        post.visibility = visibility;
        return post;
    }
}
