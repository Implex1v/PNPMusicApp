package me.toelke.pnpmusicapp.api.song

import org.springframework.stereotype.Service

@Service
class SongService(
    val songRepository: SongRepository
) {
    fun getAll() = songRepository.findAll()
}