package com.zeeb.luna.ui.screens.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.zeeb.luna.data.db.entity.SearchHistoryEntity
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.MediaItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MediaRepository(application)

    val searchQuery = MutableStateFlow("")

    val searchResults: Flow<PagingData<MediaItem>> = searchQuery
        .debounce(300)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) flowOf(PagingData.empty())
            else repository.searchMediaPaged(query)
        }
        .cachedIn(viewModelScope)

    val recentSearches: StateFlow<List<SearchHistoryEntity>> = repository
        .getRecentSearches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onSearch(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch { repository.saveSearch(query) }
        }
    }

    fun deleteSearch(query: String) {
        viewModelScope.launch { repository.deleteSearch(query) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearSearchHistory() }
    }
}
