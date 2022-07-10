package ccc.keeweapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ccc")
@EnableFeignClients("ccc")
public class KeeweApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeeweApiApplication.class, args);
    }
}
