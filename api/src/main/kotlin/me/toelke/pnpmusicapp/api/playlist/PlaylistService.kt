package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.AbstractService
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PlaylistService(
    private val repository: PlaylistRepository
): AbstractService<Playlist>(repository) {
    override fun getAll(pageable: Pageable, searchFilter: SearchFilter): Flux<PageableResult<Playlist>>
        = repository.find(pageable, searchFilter)
}