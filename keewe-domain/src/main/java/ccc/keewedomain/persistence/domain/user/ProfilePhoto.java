package ccc.keewedomain.persistence.domain.user;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "profile_photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfilePhoto extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_photo_id")
    private Long id;

    @Column(name = "image")
    private String image;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public static ProfilePhoto of(String image) {
        ProfilePhoto entity = new ProfilePhoto();
        entity.image = image;
        entity.deleted = false;
        return entity;
    }

    public void delete() {
        this.deleted = true;
    }
}
