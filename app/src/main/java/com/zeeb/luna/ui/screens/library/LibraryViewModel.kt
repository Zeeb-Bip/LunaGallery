package com.zeeb.luna.ui.screens.library

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.zeeb.luna.data.repository.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MediaRepository(application)

    val mediaPaged = repository.getAllMediaPaged().cachedIn(viewModelScope)

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    val isSelectionMode get() = _selectedIds.value.isNotEmpty()

    fun toggleSelection(id: Long) {
        _selectedIds.value = _selectedIds.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }
    }

    fun clearSelection() {
        _selectedIds.value = emptySet()
    }

    fun deleteSelected() {
        viewModelScope.launch {
            _selectedIds.value.forEach { repository.moveToTrash(it) }
            clearSelection()
        }
    }

    fun favoriteSelected() {
        viewModelScope.launch {
            _selectedIds.value.forEach { repository.toggleFavorite(it, true) }
            clearSelection()
        }
    }

    fun syncMedia() {
        viewModelScope.launch {
            repository.syncMediaStore()
        }
    }
}
