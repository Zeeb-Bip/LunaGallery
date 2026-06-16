package com.zeeb.luna.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.zeeb.luna.data.db.entity.MediaItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao {
	    @Query("SELECT * FROM media_items WHERE isDeleted = 0 ORDER BY dateTaken DESC")
	        fun getAllPaged(): PagingSource<Int, MediaItemEntity>

	            @Query("SELECT * FROM media_items WHERE isFavorite = 1 AND isDeleted = 0 ORDER BY dateTaken DESC")
	                fun getFavoritesPaged(): PagingSource<Int, MediaItemEntity>

	                    @Query("SELECT * FROM media_items WHERE isDeleted = 1 ORDER BY deletedAt DESC")
	                        fun getDeletedPaged(): PagingSource<Int, MediaItemEntity>

	                            @Query("SELECT * FROM media_items WHERE bucketId = :bucketId AND isDeleted = 0 ORDER BY dateTaken DESC")
	                                fun getByAlbumPaged(bucketId: Long): PagingSource<Int, MediaItemEntity>

	                                    @Query("SELECT * FROM media_items WHERE (name LIKE '%' || :query || '%' OR bucketName LIKE '%' || :query || '%') AND isDeleted = 0")
	                                        fun searchMedia(query: String): PagingSource<Int, MediaItemEntity>

	                                            @Query("SELECT * FROM media_items WHERE id = :id")
	                                                suspend fun getById(id: Long): MediaItemEntity?

	                                                    @Insert(onConflict = OnConflictStrategy.REPLACE)
	                                                        suspend fun insertAll(mediaItems: List<MediaItemEntity>)

	                                                            @Query("UPDATE media_items SET isFavorite = :isFavorite WHERE id = :id")
	                                                                suspend fun updateFavorite(id: Long, isFavorite: Boolean)

	                                                                    @Query("UPDATE media_items SET isDeleted = :isDeleted, deletedAt = :deletedAt WHERE id = :id")
	                                                                        suspend fun updateDeleteStatus(id: Long, isDeleted: Boolean, deletedAt: Long?)

	                                                                            @Query("DELETE FROM media_items WHERE id = :id")
	                                                                                suspend fun deletePermanently(id: Long)

	                                                                                    @Query("DELETE FROM media_items WHERE isDeleted = 1 AND deletedAt < :threshold")
	                                                                                        suspend fun cleanupOldTrash(threshold: Long)

	                                                                                            @Query("SELECT id FROM media_items")
	                                                                                                suspend fun getAllIds(): List<Long>
	                                                                                                }
}
