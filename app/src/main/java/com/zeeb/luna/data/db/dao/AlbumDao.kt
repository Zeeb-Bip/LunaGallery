package com.zeeb.luna.data.db.dao

import androidx.room.*
import com.zeeb.luna.data.db.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
	    @Query("SELECT * FROM albums ORDER BY isManual DESC, name ASC")
	        fun getAllAlbums(): Flow<List<AlbumEntity>>

	            @Insert(onConflict = OnConflictStrategy.REPLACE)
	                suspend fun insertAlbums(albums: List<AlbumEntity>)

	                    @Query("DELETE FROM albums WHERE id = :id")
	                        suspend fun deleteAlbum(id: Long)

	                            @Query("DELETE FROM albums WHERE id NOT IN (:currentBucketIds) AND isManual = 0")
	                                suspend fun deleteOrphanedAlbums(currentBucketIds: List<Long>)
	                                }
}
