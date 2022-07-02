package ccc.bridgedomain.user;

import javax.persistence.*;

@Entity
@Table(name = "profile_photo")
public class ProfilePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_photo_id")
    private Long id;
}
