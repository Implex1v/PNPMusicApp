package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.AbstractPageableRepository
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CustomPlaylistRepositoryImpl(
    template: ReactiveMongoTemplate
): CustomPlaylistRepository, AbstractPageableRepository<Playlist>(template) {
    override fun find(pageable: Pageable, searchFilter: SearchFilter): Flux<PageableResult<Playlist>>
        = find(pageable, searchFilter, Playlist::class.java)
}