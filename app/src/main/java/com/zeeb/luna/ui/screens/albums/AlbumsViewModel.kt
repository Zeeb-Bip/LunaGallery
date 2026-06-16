package com.zeeb.luna.ui.screens.albums

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.Album
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AlbumsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MediaRepository(application)

    val albums = repository.getAllAlbums().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val favoritesPaged = repository.getFavoritesPaged().cachedIn(viewModelScope)
    val deletedPaged = repository.getDeletedMediaPaged().cachedIn(viewModelScope)
    val videosPaged = repository.getVideosPaged().cachedIn(viewModelScope)
    val rawPaged = repository.getRawMediaPaged().cachedIn(viewModelScope)

    fun restoreFromTrash(id: Long) {
        viewModelScope.launch { repository.restoreFromTrash(id) }
    }

    fun deletePermanently(id: Long) {
        viewModelScope.launch { repository.deletePermanently(id) }
    }
}
