package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.Helper
import me.toelke.pnpmusicapp.api.NotFoundException
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.uuid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/playlist")
class PlaylistController(
    val service: PlaylistService
) {
    @PostMapping
    fun create(@RequestBody body: Playlist) = service.create(obj = body.copy(id = uuid()))

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = service.get(id = id)
        .switchIfEmpty(Mono.error(NotFoundException("Song")))

    @GetMapping
    fun getAll(pageable: Pageable, searchFilter: SearchFilter): Mono<ResponseEntity<Flux<Playlist>>>  {
        return service.getAll(pageable = pageable, searchFilter = searchFilter).flatMap { result ->
            result.total.flatMap {
                val resp = ResponseEntity
                    .ok()
                    .header(Helper.XTotalCount, it.toString())
                    .body(result.items)
                Mono.just(resp)
            }
        }.single()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody body: Playlist) = service.update(obj = body.copy(id = id))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = service.delete(id)
}