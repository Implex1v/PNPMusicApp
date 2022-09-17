package me.toelke.pnpmusicapp.api.song.file

import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.file.Path
import kotlin.io.path.createDirectories

@Service
class FileManager {
    private val location = "/tmp/app"

    fun writeFile(fileName: String, content: Mono<FilePart>): Mono<Void> {
        val path = Path.of(location).run {
            createDirectories()
            resolve(fileName)
        }

        return content.flatMap {
            DataBufferUtils.write(it.content(), path)
        }.then()
    }

    fun readFile(fileName: String): Flux<DataBuffer> {
        val path = Path.of(location).run {
            createDirectories()
            resolve(fileName)
        }
        return DataBufferUtils.read(path, DefaultDataBufferFactory(), 4096)
    }
}