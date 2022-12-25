package ccc.keewepush;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ccc")
public class KeewePushApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeewePushApplication.class, args);
    }

}
