package com.zeeb.luna.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.zeeb.luna.data.db.LunaDatabase
import com.zeeb.luna.data.db.entity.AlbumEntity
import com.zeeb.luna.domain.model.Album
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.MediaStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MediaRepository(private val context: Context) {

    private val db = LunaDatabase.getInstance(context)
    private val mediaDao = db.mediaDao()
    private val albumDao = db.albumDao()
    private val searchHistoryDao = db.searchHistoryDao()

    private val pagingConfig = PagingConfig(
        pageSize = 60,
        prefetchDistance = 30,
        enablePlaceholders = true
    )

    fun getAllMediaPaged(): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getAllMediaPaged()
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getFavoritesPaged(): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getFavoritesPaged()
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getDeletedMediaPaged(): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getDeletedMediaPaged()
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getMediaByBucketPaged(bucketId: Long): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getMediaByBucketPaged(bucketId)
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun searchMediaPaged(query: String): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.searchMediaPaged("%$query%")
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getVideosPaged(): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getVideosPaged()
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getRawMediaPaged(): Flow<PagingData<MediaItem>> = Pager(pagingConfig) {
        mediaDao.getRawMediaPaged()
    }.flow.map { it.map { entity -> entity.toDomain() } }

    fun getAllAlbums(): Flow<List<Album>> =
        albumDao.getAllAlbums().map { list -> list.map { it.toDomain() } }

    fun getRecentSearches() = searchHistoryDao.getRecentSearches()

    suspend fun saveSearch(query: String) = withContext(Dispatchers.IO) {
        searchHistoryDao.insert(
            com.zeeb.luna.data.db.entity.SearchHistoryEntity(
                query = query,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun deleteSearch(query: String) = withContext(Dispatchers.IO) {
        searchHistoryDao.deleteByQuery(query)
    }

    suspend fun clearSearchHistory() = withContext(Dispatchers.IO) {
        searchHistoryDao.clearHistory()
    }

    suspend fun syncMediaStore() = withContext(Dispatchers.IO) {
        val allMedia = MediaStoreUtil.queryAllMedia(context)
        mediaDao.upsertAll(allMedia)

        val currentIds = allMedia.map { it.id }
        if (currentIds.isNotEmpty()) {
            mediaDao.removeOrphaned(currentIds)
        }

        val albumMap = MediaStoreUtil.buildAlbumsFromMedia(allMedia)
        val albumEntities = albumMap.entries.mapIndexed { _, entry ->
            AlbumEntity(
                id = entry.key,
                name = entry.value.first,
                coverUri = entry.value.second,
                itemCount = allMedia.count { it.bucketId == entry.key },
                totalSize = entry.value.third,
                isManual = false
            )
        }
        albumDao.upsertAll(albumEntities)
        albumDao.deleteOrphanedAlbums(albumMap.keys.toList())
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        mediaDao.setFavorite(id, isFavorite)
    }

    suspend fun moveToTrash(id: Long) = withContext(Dispatchers.IO) {
        mediaDao.softDelete(id, System.currentTimeMillis())
    }

    suspend fun restoreFromTrash(id: Long) = withContext(Dispatchers.IO) {
        mediaDao.restore(id)
    }

    suspend fun deletePermanently(id: Long) = withContext(Dispatchers.IO) {
        val item = mediaDao.getById(id) ?: return@withContext
        try {
            context.contentResolver.delete(
                android.net.Uri.parse(item.uri), null, null
            )
        } catch (e: Exception) { /* file mungkin sudah terhapus */ }
        mediaDao.deletePermanently(id)
    }

    suspend fun purgeExpiredTrash(retentionDays: Int) = withContext(Dispatchers.IO) {
        val cutoff = System.currentTimeMillis() - (retentionDays * 24 * 60 * 60 * 1000L)
        mediaDao.purgeExpiredTrash(cutoff)
    }

    suspend fun getById(id: Long): MediaItem? = withContext(Dispatchers.IO) {
        mediaDao.getById(id)?.toDomain()
    }

}

