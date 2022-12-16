package me.toelke.pnpmusicapp.api

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NoMp3Exception(throwable: Throwable): ResponseStatusException(HttpStatus.BAD_REQUEST, "uploaded file is not a mp3", throwable)