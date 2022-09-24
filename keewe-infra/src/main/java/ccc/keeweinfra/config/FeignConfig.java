package ccc.keeweinfra.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("ccc")
public class FeignConfig {
}
