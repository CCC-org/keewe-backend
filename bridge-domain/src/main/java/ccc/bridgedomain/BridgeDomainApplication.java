package ccc.bridgedomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BridgeDomainApplication {

    public static void main(String[] args) {
        SpringApplication.run(BridgeDomainApplication.class, args);
    }

}
