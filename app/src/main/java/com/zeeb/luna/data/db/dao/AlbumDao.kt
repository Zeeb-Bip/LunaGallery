package com.zeeb.luna.data.db.dao

import androidx.room.*
import com.zeeb.luna.data.db.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM albums ORDER BY name ASC")
    fun getAllAlbums(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE id = :id")
    suspend fun getById(id: Long): AlbumEntity?

    @Upsert
    suspend fun upsertAll(albums: List<AlbumEntity>)

    @Upsert
    suspend fun upsert(album: AlbumEntity)

    @Query("DELETE FROM albums WHERE id = :id AND isManual = 1")
    suspend fun deleteManualAlbum(id: Long)

    @Query("DELETE FROM albums WHERE id NOT IN (:currentBucketIds) AND isManual = 0")
    suspend fun deleteOrphanedAlbums(currentBucketIds: List<Long>)
}
