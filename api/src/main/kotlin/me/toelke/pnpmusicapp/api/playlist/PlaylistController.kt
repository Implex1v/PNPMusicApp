package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.Helper
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.uuid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
@RestController
@RequestMapping("/playlist")
class PlaylistController(
    val service: PlaylistService
) {
    @PostMapping
    suspend fun create(@RequestBody body: Playlist): Playlist = service.create(obj = body.copy(id = uuid()))

    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: String) = service.get(id = id) ?: ResponseStatusException(HttpStatus.NOT_FOUND)

    @GetMapping
    suspend fun getAll(pageable: Pageable, searchFilter: SearchFilter): ResponseEntity<List<Playlist>> =
        service.getAll(pageable = pageable, searchFilter = searchFilter).let {
            ResponseEntity
                .ok()
                .header(Helper.XTotalCount, it.toString())
                .body(it.items)
        }

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, @RequestBody body: Playlist): Playlist =
        service.update(obj = body.copy(id = id))

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) = service.delete(id)
}

