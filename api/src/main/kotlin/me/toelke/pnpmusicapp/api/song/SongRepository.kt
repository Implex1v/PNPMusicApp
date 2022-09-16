package me.toelke.pnpmusicapp.api.song

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface SongRepository: ReactiveMongoRepository<Song, String> {
}