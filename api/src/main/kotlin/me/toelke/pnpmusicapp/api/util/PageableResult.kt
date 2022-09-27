package me.toelke.pnpmusicapp.api.util

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class PageableResult<T>(val items: Flux<T>, val total: Mono<Long>)