package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.util.AbstractService
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PlaylistService(
    private val repository: PlaylistRepository
): AbstractService<Playlist>(repository) {
    override suspend fun getAll(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Playlist>
        = repository.find(pageable, searchFilter)
}