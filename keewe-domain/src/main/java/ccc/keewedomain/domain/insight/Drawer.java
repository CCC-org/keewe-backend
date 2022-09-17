package ccc.keewedomain.domain.insight;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drawer")
public class Drawer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "drawer_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "drawer")
    private List<Insight> insights = new ArrayList<>();
}
