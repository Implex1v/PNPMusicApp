package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.uuid
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class SongService(
    val songRepository: SongRepository,
    val fileManager: FileManager,
) {
    fun getAll() = songRepository.findAll()
    fun create(song: Song) = songRepository.insert(song.copy(id = uuid()))
    fun get(id: String) = songRepository.findById(id)
    fun delete(id: String) = songRepository.deleteById(id)
    fun update(song: Song) = songRepository.save(song)
    fun createFile(id: String, file: Mono<FilePart>): Mono<Void> {
        val fileName = "$id.mp3"
        // TODO validate it's a MP3 and extract metadata
        return fileManager.writeFile(fileName, file)
    }

    fun getFile(id: String): Flux<DataBuffer> {
        val fileName = "$id.mp3"
        return fileManager.readFile(fileName)
    }
}