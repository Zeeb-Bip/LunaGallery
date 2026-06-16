package com.zeeb.luna.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.zeeb.luna.data.db.LunaDatabase
import com.zeeb.luna.data.db.entity.SearchHistoryEntity
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.MediaItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SearchFilter { ALL, PHOTOS, VIDEOS, FAVORITES, RAW }

class SearchViewModel(
	    private val repository: MediaRepository,
	        private val db: LunaDatabase
	        ) : ViewModel() {

	        	    private val searchHistoryDao = db.searchHistoryDao()
	        	        private val mediaDao = db.mediaDao()

	        	            private val _searchQuery = MutableStateFlow("")
	        	                val searchQuery = _searchQuery.asStateFlow()

	        	                    private val _activeFilter = MutableStateFlow(SearchFilter.ALL)
	        	                        val activeFilter = _activeFilter.asStateFlow()

	        	                            val recentSearches: StateFlow<List<SearchHistoryEntity>> = searchHistoryDao.getRecentSearch()
	        	                                    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

	        	                                        @OptIn(ExperimentalCoroutinesApi::class)
	        	                                            val searchResults: Flow<PagingData<MediaItem>> = combine(_searchQuery, _activeFilter) { query, filter ->
	        	                                                    Pair(query, filter)
	        	                                                        }.flatMapLatest { (query, filter) ->
	        	                                                                Pager(PagingConfig(pageSize = 50)) {
	        	                                                                	            mediaDao.searchMedia(query)
	        	                                                                	                    }.flow.map { pagingData ->
	        	                                                                	                                pagingData.map { it.toDomain() }.filterMedia(filter)
	        	                                                                	                                        }
	        	                                                                	                                            }.cachedIn(viewModelScope)

	        	                                                                	                                                fun onQueryChange(newQuery: String) {
	        	                                                                	                                                	        _searchQuery.value = newQuery
	        	                                                                	                                                	            }

	        	                                                                	                                                	                fun onFilterChange(filter: SearchFilter) {
	        	                                                                	                                                	                	        _activeFilter.value = filter
	        	                                                                	                                                	                	            }

	        	                                                                	                                                	                	                fun saveSearch(query: String) {
	        	                                                                	                                                	                	                	        if (query.isBlank()) return
	        	                                                                	                                                	                	                	                viewModelScope.launch {
	        	                                                                	                                                	                	                	                	            searchHistoryDao.insertSearch(SearchHistoryEntity(query = query, timestamp = System.currentTimeMillis()))
	        	                                                                	                                                	                	                	                	                    }
	        	                                                                	                                                	                	                	                	                        }

	        	                                                                	                                                	                	                	                	                            fun clearHistory() {
	        	                                                                	                                                	                	                	                	                            	        viewModelScope.launch {
	        	                                                                	                                                	                	                	                	                            	        	            searchHistoryDao.clearHistory()
	        	                                                                	                                                	                	                	                	                            	        	                    }
	        	                                                                	                                                	                	                	                	                            	        	                        }

	        	                                                                	                                                	                	                	                	                            	        	                            private fun PagingData<MediaItem>.filterMedia(filter: SearchFilter): PagingData<MediaItem> {
	        	                                                                	                                                	                	                	                	                            	        	                            	        return when (filter) {
	        	                                                                	                                                	                	                	                	                            	        	                            	        	            SearchFilter.ALL -> this
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                        SearchFilter.PHOTOS -> this.filter { !it.isVideo }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                    SearchFilter.VIDEOS -> this.filter { it.isVideo }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                SearchFilter.FAVORITES -> this.filter { it.isFavorite }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                            SearchFilter.RAW -> this.filter { it.isRaw }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                    }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                        }

	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                            // Extension mapper dari entity ke domain (helper)
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                                private fun com.zeeb.luna.data.db.entity.MediaItemEntity.toDomain() = MediaItem(
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                                	        id, android.net.Uri.parse(uri), path, name, mimeType, size, width, height, duration, dateAdded, dateTaken, bucketId, bucketName, isFavorite, isDeleted, deletedAt, mediaType
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                                	            )
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                                	            }
	        	                                                                	                                                	                	                	                	                            	        	                            	        	                                                                                )
	        	                                                                	                                                	                	                	                	                            	        	                            	        }
	        	                                                                	                                                	                	                	                	                            	        	                            }
	        	                                                                	                                                	                	                	                	                            	        }
	        	                                                                	                                                	                	                	                	                            }
	        	                                                                	                                                	                	                	                }
	        	                                                                	                                                	                	                }
	        	                                                                	                                                	                }
	        	                                                                	                                                }
	        	                                                                }}
	        }
)
