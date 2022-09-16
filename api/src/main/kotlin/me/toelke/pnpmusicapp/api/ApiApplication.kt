package me.toelke.pnpmusicapp.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@ConfigurationPropertiesScan("me.toelke.pnpmusicapp.api")
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
