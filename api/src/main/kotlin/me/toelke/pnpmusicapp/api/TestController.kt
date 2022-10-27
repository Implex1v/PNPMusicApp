package me.toelke.pnpmusicapp.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class TestController() {
   @GetMapping("test")
   fun test() = "test"
}

