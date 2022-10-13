package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import reactor.core.publisher.Flux

interface CustomSongRepository {
    fun find(pageable: Pageable, searchFilter: SearchFilter): Flux<PageableResult<Song>>
}