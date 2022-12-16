package me.toelke.pnpmusicapp.api.song.file

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting

@Service
class FileManager {
    private val location = "/tmp/app"

    suspend fun writeFile(fileName: String, content: FilePart) {
        val path = getPath(fileName)
        DataBufferUtils.write(content.content(), path).awaitSingleOrNull()
    }

    suspend fun readFile(fileName: String): List<DataBuffer> =
        try {
            val path = getPath(fileName)
            DataBufferUtils.read(path, DefaultDataBufferFactory(), 4096).collectList().awaitSingle()
        } catch (ex: Exception) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND)
        }

    suspend fun getPath(fileName: String): Path = Path.of(location).run {
        createDirectories()
        resolve(fileName)
    }

    suspend fun deleteFile(fileName: String) {
        val path = getPath(fileName)
        path.deleteExisting()
    }
}