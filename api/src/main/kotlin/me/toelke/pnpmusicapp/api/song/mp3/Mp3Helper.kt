package me.toelke.pnpmusicapp.api.song.mp3

import com.mpatric.mp3agic.Mp3File

fun Mp3File.artist(): String = this.run {
    when {
        hasId3v2Tag() -> id3v2Tag.artist  ?: "unknown"
        hasId3v1Tag() -> id3v1Tag.artist  ?: "unknown"
        else -> "unknown"
    }
}

fun Mp3File.title(): String = this.run {
    when {
        hasId3v2Tag() -> id3v2Tag.title ?: "unknown"
        hasId3v1Tag() -> id3v1Tag.title ?: "unknown"
        else -> "unknown"
    }
}

fun Mp3File.album(): String? = this.run {
    when {
        hasId3v2Tag() -> id3v2Tag.album
        hasId3v1Tag() -> id3v1Tag.album
        else -> null
    }
}
