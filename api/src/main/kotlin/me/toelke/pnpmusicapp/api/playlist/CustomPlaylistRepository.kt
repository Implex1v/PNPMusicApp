package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CustomPlaylistRepository {
    suspend fun find(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Playlist>
}