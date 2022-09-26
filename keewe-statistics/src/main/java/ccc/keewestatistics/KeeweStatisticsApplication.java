package ccc.keewestatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ccc")
public class KeeweStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeeweStatisticsApplication.class, args);
    }

}
