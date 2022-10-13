package me.toelke.pnpmusicapp.api.util

import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

abstract class AbstractService<T : Any>(
    private val repository: ReactiveMongoRepository<T, String>
) {
    fun create(obj: T) = repository.insert(obj)
    fun get(id: String) = repository.findById(id)
    fun update(obj: T) = repository.save(obj)
    fun delete(id: String) = repository.deleteById(id)

    abstract fun getAll(pageable: Pageable, searchFilter: SearchFilter): Flux<PageableResult<T>>
}