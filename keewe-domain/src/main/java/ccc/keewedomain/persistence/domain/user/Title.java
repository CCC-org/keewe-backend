package ccc.keewedomain.persistence.domain.user;

import ccc.keewedomain.persistence.domain.common.BaseTimeEntity;
import ccc.keewedomain.persistence.domain.user.enums.TitleCategory;

import javax.persistence.*;

@Entity
public class Title extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TitleCategory category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "introduction", nullable = false)
    private String introduction;

    @Column(name = "image")
    private String image;
}
