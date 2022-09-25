package me.toelke.pnpmusicapp.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver

@Configuration
class WebConfig {
    @Bean
    fun reactivePageableHandler() = ReactivePageableHandlerMethodArgumentResolver()

    @Bean
    fun searchFilterHandler() = SearchFilterArgumentResolver()
}