package me.toelke.pnpmusicapp.api.util

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractService<T : Any>(
    private val repository: ReactiveMongoRepository<T, String>
) {
    suspend fun create(obj: T): T = repository.insert(obj).awaitSingle()
    suspend fun get(id: String): T? = repository.findById(id).awaitSingleOrNull()
    suspend fun update(obj: T): T = repository.save(obj).awaitSingle()
    suspend fun delete(id: String) = repository.deleteById(id).awaitSingleOrNull()

    abstract suspend fun getAll(pageable: Pageable, searchFilter: SearchFilter): PageableResult<T>
}