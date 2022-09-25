package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux

class CustomSongRepositoryImpl(
    private val template: ReactiveMongoTemplate
): CustomSongRepository {
    override fun find(pageable: Pageable, searchFilter: SearchFilter): Flux<Song> {
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

        return template.find(query, Song::class.java)
    }
}