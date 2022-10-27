package me.toelke.pnpmusicapp.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class ActuatorConfig {
    @Bean
    public fun actuatorWebFilterChain(http: ServerHttpSecurity ): SecurityWebFilterChain =
        http
            .authorizeExchange()
            .pathMatchers("/actuator/health", "/actuator/prometheus")
            .permitAll()
            .anyExchange()
            .permitAll()
            .and().build()
}