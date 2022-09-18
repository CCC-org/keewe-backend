package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import ccc.keewedomain.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "drawer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Drawer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "drawer_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "drawer")
    private List<Insight> insights = new ArrayList<>();

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    public static Drawer of(User user, String name) {
        Drawer drawer = new Drawer();
        drawer.user = user;
        drawer.name = name;

        return drawer;
    }
}
