package ccc.keewedomain.persistence.domain.misc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "keewe_config")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KeeweConfig {
    @Id
    @Column(name = "config_key")
    private String key;

    @Column(name = "value")
    private String value;
}
