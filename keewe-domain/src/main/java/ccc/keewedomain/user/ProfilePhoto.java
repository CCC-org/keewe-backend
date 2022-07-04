package ccc.keewedomain.user;

import ccc.keewedomain.common.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Table(name = "profile_photo")
public class ProfilePhoto extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_photo_id")
    private Long id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
}
