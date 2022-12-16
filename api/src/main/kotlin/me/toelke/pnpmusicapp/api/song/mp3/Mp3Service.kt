package me.toelke.pnpmusicapp.api.song.mp3

import com.mpatric.mp3agic.Mp3File
import me.toelke.pnpmusicapp.api.NoMp3Exception
import me.toelke.pnpmusicapp.api.song.SongDetail
import me.toelke.pnpmusicapp.api.song.file.FileManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class Mp3Service(
    private val fileManager: FileManager
) {
    suspend fun parseMp3(file: String): SongDetail =
        try {
            val mp3 = Mp3File(fileManager.getPath(file))
            SongDetail(
                seconds = mp3.lengthInSeconds.toInt(),
                artist = mp3.artist(),
                title = mp3.title(),
                album = mp3.album(),
            )
        } catch (ex: Exception) {
            throw NoMp3Exception(ex)
        }
}