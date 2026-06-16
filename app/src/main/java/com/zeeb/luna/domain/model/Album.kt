package com.zeeb.luna.domain.model

data class Album(
    val id: Long,
    val name: String,
    val coverUri: String,
    val itemCount: Int,
    val totalSize: Long,
    val isManual: Boolean = false
)
