package ccc.keewedomain.domain.nest;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.user.Profile;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name="nest")
public class Nest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nest_id")
    private Long id;

    @OneToOne(mappedBy = "nest", fetch = LAZY)
    private Profile profile;

    @OneToMany(mappedBy = "nest", fetch = LAZY)
    private List<Post> posts = new ArrayList<>();
}
