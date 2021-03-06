package ccc.keewedomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class KeeweDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeeweDomainApplication.class, args);
    }

}
