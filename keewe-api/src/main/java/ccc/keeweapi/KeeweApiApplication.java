package ccc.keeweapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan({"ccc.keewedomain", "ccc.keeweapi"})
@EnableJpaAuditing
public class KeeweApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeeweApiApplication.class, args);
    }

}
