package com.zeeb.luna.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeeb.luna.domain.model.Album

@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val coverUri: String,
    val itemCount: Int,
    val totalSize: Long,
    val isManual: Boolean = false
) {
    fun toDomain() = Album(
        id = id, name = name, coverUri = coverUri,
        itemCount = itemCount, totalSize = totalSize, isManual = isManual
    )
}

fun Album.toEntity() = AlbumEntity(
    id = id, name = name, coverUri = coverUri,
    itemCount = itemCount, totalSize = totalSize, isManual = isManual
)
