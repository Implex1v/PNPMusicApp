package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.NotFoundException
import me.toelke.pnpmusicapp.api.config.SearchFilter
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/song")
class SongController(
    val service: SongService,
) {
    @GetMapping
    fun getAll(pageable: Pageable, searchFilter: SearchFilter): Mono<ResponseEntity<Flux<Song>>>  {
        val data = service.getAll(pageable = pageable, searchFilter = searchFilter)

        return data.total.flatMap {
            val resp = ResponseEntity
                .ok()
                .header("x-total-count", it.toString())
                .body(data.items)
            Mono.just(resp)
        }
    }

    @PostMapping
    fun create(@RequestBody body: Song) = service.create(song = body)

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = service.get(id = id)
        .switchIfEmpty(Mono.error(NotFoundException("Song")))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody body: Song) = service.update(song = body.copy(id = id))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = service.delete(id)

    @PostMapping("/{id}/file")
    fun createFile(@PathVariable id: String, @RequestPart("file") file: Mono<FilePart>) = service.createFile(id, file)

    @GetMapping("/{id}/file", produces = ["audio/mpeg"])
    fun getFile(@PathVariable id: String) = service.getFile(id)
}