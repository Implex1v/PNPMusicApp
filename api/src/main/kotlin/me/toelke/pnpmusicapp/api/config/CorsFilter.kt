package me.toelke.pnpmusicapp.api.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Configuration
class CorsFilter: WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request: ServerHttpRequest = exchange.request
        val response: ServerHttpResponse = exchange.response
        val headers: HttpHeaders = response.headers

        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, PUT, OPTIONS, DELETE, PATCH")
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
        headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*")
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "*")
        headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600L")

        if (request.method === HttpMethod.OPTIONS) {
            response.statusCode = HttpStatus.OK
            return Mono.empty()
        }

        return chain.filter(exchange)
    }
}