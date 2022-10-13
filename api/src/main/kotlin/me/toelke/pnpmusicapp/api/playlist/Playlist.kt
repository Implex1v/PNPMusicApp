package me.toelke.pnpmusicapp.api.playlist

import me.toelke.pnpmusicapp.api.uuid
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Playlist(
    @Id
    val id: String = uuid(),
    val name: String,
    val tags: List<String> = emptyList(),
    val songs: List<ObjectId> = emptyList(),
)