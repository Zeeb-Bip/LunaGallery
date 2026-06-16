package com.zeeb.luna.domain.model

data class MediaItem(
    val id: Long,
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
    val isVideo: Boolean get() = mimeType.startsWith("video/")
    val isImage: Boolean get() = mimeType.startsWith("image/")
    val isRaw: Boolean get() = mimeType.contains("x-adobe-dng") ||
        mimeType.contains("x-canon-cr2") ||
        mimeType.contains("x-nikon-nef") ||
        mimeType.contains("x-sony-arw")
    val isGif: Boolean get() = mimeType == "image/gif"
    val isSvg: Boolean get() = mimeType == "image/svg+xml"
}
