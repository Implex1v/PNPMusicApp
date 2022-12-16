package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.config.SearchFilter
import me.toelke.pnpmusicapp.api.song.file.FileManager
import me.toelke.pnpmusicapp.api.song.mp3.Mp3Service
import me.toelke.pnpmusicapp.api.util.AbstractService
import me.toelke.pnpmusicapp.api.util.PageableResult
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.codec.multipart.FilePart
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.lang.Exception

@Service
class SongService(
    val songRepository: SongRepository,
    val fileManager: FileManager,
    val mp3Service: Mp3Service,
): AbstractService<Song>(songRepository) {
    override suspend fun getAll(pageable: Pageable, searchFilter: SearchFilter): PageableResult<Song>
        = songRepository.find(pageable, searchFilter)

    suspend fun createFile(id: String, file: FilePart) {
        val fileName = "$id.mp3"

        try {
            fileManager.writeFile(fileName, file)
            val song = get(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
            val detail = mp3Service.parseMp3(fileName)
            update(song.copy(detail = detail))
        } catch (ex: Exception) {
            fileManager.deleteFile(fileName)
            when (ex) {
                is ResponseStatusException -> throw ex
                else -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload mp3 file")
            }
        }
    }

    suspend fun getFile(id: String): List<DataBuffer> {
        val fileName = "$id.mp3"
        return fileManager.readFile(fileName)
    }
}