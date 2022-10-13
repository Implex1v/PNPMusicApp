package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.NotFoundException
import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.song.mp3.Mp3Service
import me.toelke.pnpmusicapp.api.util.AbstractService
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Service
class SongService(
    val songRepository: SongRepository,
    val fileManager: FileManager,
    val mp3Service: Mp3Service,
): AbstractService<Song>(songRepository) {
    override fun getAll(pageable: Pageable, searchFilter: SearchFilter): Flux<PageableResult<Song>>
        = songRepository.find(pageable, searchFilter)

    fun createFile(id: String, file: Mono<FilePart>): Mono<Void> {
        val fileName = "$id.mp3"
        return fileManager.writeFile(fileName, file)
            .then(Mono.just(fileName))
            .flatMap {
                val songMono = get(id)
                    .switchIfEmpty {
                        fileManager
                            .deleteFile(fileName)
                            .then(Mono.error(NotFoundException("song not found")))
                    }
                val detailMono = mp3Service.parseMp3(it)

                Mono.zip(songMono, detailMono) { song , detail ->
                    song.copy(detail = detail)
                }
            }
            .switchIfEmpty {
                fileManager
                    .deleteFile(fileName)
                    .then(Mono.error(NotFoundException("song not found")))
            }
            .flatMap { song -> update(song).then() }
    }

    fun getFile(id: String): Flux<DataBuffer> {
        val fileName = "$id.mp3"
        return fileManager.readFile(fileName)
    }
}