package me.toelke.pnpmusicapp.api

import java.util.UUID

fun uuid() = UUID.randomUUID().toString()

class Helper {
    companion object {
        const val XTotalCount = "x-total-count"
    }
}