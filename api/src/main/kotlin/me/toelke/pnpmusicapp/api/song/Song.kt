package me.toelke.pnpmusicapp.api.song

import me.toelke.pnpmusicapp.api.uuid
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Song(
    @Id
    val id: String = uuid(),
    val name: String,
    val location: String,
    val tags: List<String> = emptyList(),
)