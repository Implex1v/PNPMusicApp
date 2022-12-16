package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.util.PageableResult
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable

interface CustomSongRepository {
    suspend fun find(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Song>
}