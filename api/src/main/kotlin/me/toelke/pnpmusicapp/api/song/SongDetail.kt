package me.toelke.pnpmusicapp.api.song

data class SongDetail(
    val title: String,
    val artist: String,
    val seconds: Int,
    val album: String?,
)
