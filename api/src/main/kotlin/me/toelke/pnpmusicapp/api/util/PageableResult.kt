package me.toelke.pnpmusicapp.api.util

data class PageableResult<T>(val items: List<T>, val total: Long)