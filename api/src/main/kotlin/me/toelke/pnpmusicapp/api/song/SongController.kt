package me.toelke.pnpmusicapp.api.song

import kotlinx.coroutines.reactor.awaitSingle
import me.toelke.pnpmusicapp.api.Helper
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.uuid
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
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
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/song")
class SongController(
    val service: SongService,
) {
    @GetMapping
    suspend fun getAll(pageable: Pageable, searchFilter: SearchFilter): ResponseEntity<List<Song>> =
        service.getAll(pageable = pageable, searchFilter = searchFilter).let {
            ResponseEntity
                .ok()
                .header(Helper.XTotalCount, it.total.toString())
                .body(it.items)
        }

    @PostMapping
    suspend fun create(@RequestBody body: Song) = service.create(obj = body.copy(id = uuid()))

    @GetMapping("/{id}")
    suspend fun get(@PathVariable id: String) =
        service.get(id = id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    @PutMapping("/{id}")
    suspend fun update(@PathVariable id: String, @RequestBody body: Song) = service.update(obj = body.copy(id = id))

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: String) = service.delete(id)

    @PostMapping("/{id}/file")
    suspend fun createFile(@PathVariable id: String, @RequestPart("file") file: Mono<FilePart>) =
        service.createFile(id, file.awaitSingle())

    @GetMapping("/{id}/file", produces = ["audio/mpeg"])
    suspend fun getFile(@PathVariable id: String) = service.getFile(id)
}