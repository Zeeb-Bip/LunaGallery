package com.zeeb.luna.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.zeeb.luna.data.db.LunaDatabase
import com.zeeb.luna.data.db.entity.AlbumEntity
import com.zeeb.luna.data.db.entity.MediaItemEntity
import com.zeeb.luna.domain.model.Album
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.MediaStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import android.net.Uri

class MediaRepository(private val context: Context, private val db: LunaDatabase) {

	    private val mediaDao = db.mediaDao()
	        private val albumDao = db.albumDao()

	            fun getAllMediaPaged(): Flow<PagingData<MediaItem>> {
	            	        return Pager(PagingConfig(pageSize = 50)) {
	            	        	            mediaDao.getAllPaged()
	            	        	                    }.flow.map { pagingData -> pagingData.map { it.toDomain() } }
	            	        	                        }

	            	        	                            fun getFavoriteMediaPaged(): Flow<PagingData<MediaItem>> {
	            	        	                            	        return Pager(PagingConfig(pageSize = 50)) {
	            	        	                            	        	            mediaDao.getFavoritesPaged()
	            	        	                            	        	                    }.flow.map { pagingData -> pagingData.map { it.toDomain() } }
	            	        	                            	        	                        }

	            	        	                            	        	                            fun getMediaByAlbumPaged(bucketId: Long): Flow<PagingData<MediaItem>> {
	            	        	                            	        	                            	        return Pager(PagingConfig(pageSize = 50)) {
	            	        	                            	        	                            	        	            mediaDao.getByAlbumPaged(bucketId)
	            	        	                            	        	                            	        	                    }.flow.map { pagingData -> pagingData.map { it.toDomain() } }
	            	        	                            	        	                            	        	                        }

	            	        	                            	        	                            	        	                            fun getAlbums(): Flow<List<Album>> {
	            	        	                            	        	                            	        	                            	        return albumDao.getAllAlbums().map { entities ->
	            	        	                            	        	                            	        	                            	                    entities.map { it.toDomain() }
	            	        	                            	        	                            	        	                            	                            }
	            	        	                            	        	                            	        	                            	                                }

	            	        	                            	        	                            	        	                            	                                    suspend fun syncMediaStore() = withContext(Dispatchers.IO) {
	            	        	                            	        	                            	        	                            	                                    	        val mediaFromDevice = MediaStoreUtil.scanDeviceMedia(context)
	            	        	                            	        	                            	        	                            	                                    	                mediaDao.insertAll(mediaFromDevice)

	            	        	                            	        	                            	        	                            	                                    	                        // Sync Albums
	            	        	                            	        	                            	        	                            	                                    	                                val albums = mediaFromDevice.groupBy { it.bucketId }.map { (bucketId, items) ->
	            	        	                            	        	                            	        	                            	                                    	                                            val latest = items.maxByOrNull { it.dateTaken }
	            	        	                            	        	                            	        	                            	                                    	                                                        AlbumEntity(
	            	        	                            	        	                            	        	                            	                                    	                                                        	                id = bucketId,
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                name = latest?.bucketName ?: "Unknown",
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                coverUri = latest?.uri ?: "",
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                itemCount = items.size,
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                totalSize = items.sumOf { it.size }
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                            )
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                    }
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                            albumDao.deleteOrphanedAlbums(albums.map { it.id })
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                    albumDao.insertAlbums(albums)
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                        }

	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	        mediaDao.updateFavorite(id, isFavorite)
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	            }

	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                suspend fun deleteMedia(id: Long) = withContext(Dispatchers.IO) {
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	        mediaDao.updateDeleteStatus(id, true, System.currentTimeMillis())
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	            }

	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                private fun MediaItemEntity.toDomain() = MediaItem(
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	        id, Uri.parse(uri), path, name, mimeType, size, width, height, duration, dateAdded, dateTaken, bucketId, bucketName, isFavorite, isDeleted, deletedAt, mediaType
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	            )

	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	                private fun AlbumEntity.toDomain() = Album(
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	                	        id, name, coverUri, itemCount, totalSize, isManual
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	                	            )
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	                	            }
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                	                )
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                	                )
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            	                }
	            	        	                            	        	                            	        	                            	                                    	                                                        	                                                                                                                            }
	            	        	                            	        	                            	        	                            	                                    	                                                        )}
	            	        	                            	        	                            	        	                            	                                    }
	            	        	                            	        	                            	        	                            }
	            	        	                            	        	                            	        }
	            	        	                            	        	                            }
	            	        	                            	        }
	            	        	                            }
	            	        }
	            }
}
