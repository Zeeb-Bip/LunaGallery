package com.zeeb.luna.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zeeb.luna.domain.model.MediaItem

@Entity(tableName = "media_items")
data class MediaItemEntity(
    @PrimaryKey val id: Long,
    val uri: String,
    val path: String,
    val name: String,
    val mimeType: String,
    val size: Long,
    val width: Int,
    val height: Int,
    val duration: Long,
    val dateAdded: Long,
    val dateTaken: Long,
    val bucketId: Long,
    val bucketName: String,
    val isFavorite: Boolean = false,
    val isDeleted: Boolean = false,
    val deletedAt: Long? = null,
    val mediaType: Int = 0
) {
    fun toDomain() = MediaItem(
        id = id, uri = uri, path = path, name = name,
        mimeType = mimeType, size = size, width = width, height = height,
        duration = duration, dateAdded = dateAdded, dateTaken = dateTaken,
        bucketId = bucketId, bucketName = bucketName, isFavorite = isFavorite,
        isDeleted = isDeleted, deletedAt = deletedAt, mediaType = mediaType
    )
}

fun MediaItem.toEntity() = MediaItemEntity(
    id = id, uri = uri, path = path, name = name,
    mimeType = mimeType, size = size, width = width, height = height,
    duration = duration, dateAdded = dateAdded, dateTaken = dateTaken,
    bucketId = bucketId, bucketName = bucketName, isFavorite = isFavorite,
    isDeleted = isDeleted, deletedAt = deletedAt, mediaType = mediaType
)
