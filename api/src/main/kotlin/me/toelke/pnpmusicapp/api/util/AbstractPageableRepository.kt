package me.toelke.pnpmusicapp.api.util

import kotlinx.coroutines.reactor.awaitSingle
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.repository.NoRepositoryBean
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractPageableRepository<T>(
    private val template: ReactiveMongoTemplate
) {
    protected suspend fun find(pageable: Pageable, searchFilter: SearchFilter, clazz: Class<T>): PageableResult<T> {
        val criteria = Criteria()
        searchFilter
            .filters
            .forEach { entry ->
                criteria.andOperator(
                    entry
                        .value
                        .map { value ->
                            Criteria.where(entry.key).`is`(value)
                        }
                )
            }

        val query = Query
            .query(criteria)
            .with(pageable)

        val count = template
            .count(Query.query(criteria), clazz)
            .awaitSingle()

        val items = template
            .find(query, clazz)
            .collectList()
            .awaitSingle()

        return PageableResult(items = items, total = count)
    }
}