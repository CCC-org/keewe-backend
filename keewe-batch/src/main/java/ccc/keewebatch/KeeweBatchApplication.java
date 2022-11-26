package ccc.keewebatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan("ccc")
public class KeeweBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeeweBatchApplication.class, args);
    }

}
