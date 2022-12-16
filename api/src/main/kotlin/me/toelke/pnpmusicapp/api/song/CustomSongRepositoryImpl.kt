package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.AbstractPageableRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate

class CustomSongRepositoryImpl(
    template: ReactiveMongoTemplate
): CustomSongRepository, AbstractPageableRepository<Song>(template) {
    override suspend fun find(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Song> =
        find(pageable, searchFilter, Song::class.java)
}