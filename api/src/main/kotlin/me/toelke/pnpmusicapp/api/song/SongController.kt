package me.toelke.pnpmusicapp.api.song

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/song")
class SongController(
    val service: SongService,
) {
    @GetMapping
    fun getAll() = service.getAll()
}