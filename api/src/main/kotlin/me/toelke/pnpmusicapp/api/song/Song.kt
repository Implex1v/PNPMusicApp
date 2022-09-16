package me.toelke.pnpmusicapp.api.song

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Song(
    val id: String,
    val name: String,
    val location: String,
    val tags: List<String> = emptyList(),
)