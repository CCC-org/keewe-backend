package ccc.keewedomain.domain.insight;

import ccc.keewedomain.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drawer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Drawer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "drawer_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "drawer")
    private List<Insight> insights = new ArrayList<>();

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public static Drawer of(String name) {
        Drawer drawer = new Drawer();
        drawer.name = name;

        return drawer;
    }
}
