package me.toelke.pnpmusicapp.api.playlist

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface PlaylistRepository: ReactiveMongoRepository<Playlist, String>, CustomPlaylistRepository