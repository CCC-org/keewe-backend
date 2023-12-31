package ccc.keeweinfra.config

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CoroutineConfig {
    @Bean
    fun coroutineScope(): CoroutineScope {
        return CoroutineScope(Job())
    }
}