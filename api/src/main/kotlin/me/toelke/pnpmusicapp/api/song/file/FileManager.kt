package me.toelke.pnpmusicapp.api.song.file

import me.toelke.pnpmusicapp.api.NotFoundException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting

@Service
class FileManager {
    private val location = "/tmp/app"

    fun writeFile(fileName: String, content: Mono<FilePart>): Mono<Void> {
        return content.flatMap {
            val path = getPath(fileName)
            DataBufferUtils.write(it.content(), path)
        }
    }

    fun readFile(fileName: String): Flux<DataBuffer> {
        return try {
            val path = getPath(fileName)
            DataBufferUtils.read(path, DefaultDataBufferFactory(), 4096)
                .onErrorMap { NotFoundException("file '$fileName' not found") }
        } catch (ex: Exception) {
            Flux.error(NotFoundException("file '$fileName' not found"))
        }
    }

    fun getPath(fileName: String): Path = Path.of(location).run {
        createDirectories()
        resolve(fileName)
    }

    fun deleteFile(fileName: String): Mono<Void> {
        val path = getPath(fileName)
        path.deleteExisting()
        return Mono.empty()
    }
}