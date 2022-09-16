package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.uuid
import org.springframework.stereotype.Service

@Service
class SongService(
    val songRepository: SongRepository
) {
    fun getAll() = songRepository.findAll()
    fun create(song: Song) = songRepository.insert(song.copy(id = uuid()))
    fun get(id: String) = songRepository.findById(id)
    fun delete(id: String) = songRepository.deleteById(id)
    fun update(song: Song) = songRepository.save(song)
}