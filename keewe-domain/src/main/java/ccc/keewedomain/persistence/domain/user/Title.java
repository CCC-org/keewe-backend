package ccc.keewedomain.persistence.domain.user;

import ccc.keewecore.consts.TitleCategory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "title")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Title {

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
}
