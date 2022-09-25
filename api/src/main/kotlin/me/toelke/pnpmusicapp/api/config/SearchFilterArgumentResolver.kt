package me.toelke.pnpmusicapp.api.config

import org.springframework.core.MethodParameter
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.SyncHandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange

class SearchFilterArgumentResolver: SyncHandlerMethodArgumentResolver {
    private val ignoredParameter = listOf("page", "size", "sort")

    override fun supportsParameter(parameter: MethodParameter): Boolean =
        SearchFilter::class.java == parameter.parameterType

    override fun resolveArgumentValue(
        parameter: MethodParameter,
        bindingContext: BindingContext,
        exchange: ServerWebExchange
    ): Any {
        val map = exchange
            .request
            .queryParams
            .filter { !ignoredParameter.contains(it.key) }
        return SearchFilter(map)
    }
}