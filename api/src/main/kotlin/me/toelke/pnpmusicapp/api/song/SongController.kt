package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.NotFoundException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/song")
class SongController(
    val service: SongService,
) {
    @GetMapping
    fun getAll() = service.getAll()

    @PostMapping
    fun create(@RequestBody body: Song) = service.create(song = body)

    @GetMapping("/{id}")
    fun get(@PathVariable id: String) = service.get(id = id)
        .switchIfEmpty(Mono.error(NotFoundException("Song")))

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody body: Song) = service.update(song = body.copy(id = id))

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String) = service.delete(id)
}