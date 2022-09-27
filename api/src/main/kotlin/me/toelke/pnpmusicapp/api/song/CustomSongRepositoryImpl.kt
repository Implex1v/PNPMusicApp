package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class CustomSongRepositoryImpl(
    private val template: ReactiveMongoTemplate
): CustomSongRepository {
    override fun find(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Song> {
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

        val count = template.count(Query.query(criteria), Song::class.java)
        val items = template.find(query, Song::class.java)
        return PageableResult(items = items, total = count)
    }
}