package com.zeeb.luna.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.zeeb.luna.data.db.entity.MediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
    @Query("SELECT * FROM media_items WHERE isDeleted = 0 ORDER BY dateTaken DESC")
    fun getAllMediaPaged(): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND isFavorite = 1 ORDER BY dateTaken DESC")
    fun getFavoritesPaged(): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedMediaPaged(): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND bucketId = :bucketId ORDER BY dateTaken DESC")
    fun getMediaByBucketPaged(bucketId: Long): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND (name LIKE :query OR mimeType LIKE :query) ORDER BY dateTaken DESC")
    fun searchMediaPaged(query: String): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND mimeType LIKE 'video/%' ORDER BY dateTaken DESC")
    fun getVideosPaged(): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE isDeleted = 0 AND (mimeType LIKE 'image/x-%') ORDER BY dateTaken DESC")
    fun getRawMediaPaged(): PagingSource<Int, MediaItemEntity>

    @Query("SELECT * FROM media_items WHERE id = :id")
    suspend fun getById(id: Long): MediaItemEntity?

    @Upsert
    suspend fun upsertAll(items: List<MediaItemEntity>)

    @Query("UPDATE media_items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE media_items SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDelete(id: Long, deletedAt: Long)

    @Query("UPDATE media_items SET isDeleted = 0, deletedAt = NULL WHERE id = :id")
    suspend fun restore(id: Long)

    @Query("DELETE FROM media_items WHERE id = :id")
    suspend fun deletePermanently(id: Long)

    @Query("DELETE FROM media_items WHERE isDeleted = 1 AND deletedAt < :cutoffTime")
    suspend fun purgeExpiredTrash(cutoffTime: Long)

    @Query("SELECT id FROM media_items")
    suspend fun getAllIds(): List<Long>

    @Query("DELETE FROM media_items WHERE id NOT IN (:currentIds) AND isDeleted = 0")
    suspend fun removeOrphaned(currentIds: List<Long>)

    @Query("SELECT COUNT(*) FROM media_items WHERE isDeleted = 0")
    fun getTotalCount(): Flow<Int>
}
