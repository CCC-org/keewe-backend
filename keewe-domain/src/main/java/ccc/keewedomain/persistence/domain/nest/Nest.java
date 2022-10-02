package ccc.keewedomain.persistence.domain.nest;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name="nest")
@Getter
public class Nest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nest_id")
    private Long id;

//    @OneToMany(mappedBy = "nest", fetch = LAZY)
//    private List<Post> posts = new ArrayList<>();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public static Nest of() {
        Nest nest = new Nest();
        return nest;
    }
}
