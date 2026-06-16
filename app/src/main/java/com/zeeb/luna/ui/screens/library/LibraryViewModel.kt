package com.zeeb.luna.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.FormatUtil
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class LibraryItem {
	    data class Media(val media: MediaItem) : LibraryItem()
	        data class Header(val date: String) : LibraryItem()
	        }

	        class LibraryViewModel(private val repository: MediaRepository) : ViewModel() {

	        	    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
	        	        val selectedIds: StateFlow<Set<Long>> = _selectedIds.asStateFlow()

	        	            private val _isSelectionMode = MutableStateFlow(false)
	        	                val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

	        	                    val mediaFlow: Flow<PagingData<LibraryItem>> = repository.getAllMediaPaged()
	        	                            .map { pagingData ->
	        	                                        pagingData.map { LibraryItem.Media(it) as LibraryItem }
	        	                                                        .insertSeparators { before, after ->
	        	                                                                            if (after is LibraryItem.Media && (before == null || 
	        	                                                                                                    FormatUtil.formatHeaderDate((before as LibraryItem.Media).media.dateTaken) != 
	        	                                                                                                                            FormatUtil.formatHeaderDate(after.media.dateTaken))) {
	        	                                                                                                                            	                        LibraryItem.Header(FormatUtil.formatHeaderDate(after.media.dateTaken))
	        	                                                                                                                            	                                            } else {
	        	                                                                                                                            	                                            	                        null
	        	                                                                                                                            	                                            	                                            }
	        	                                                                                                                            	                                            	                                                            }
	        	                                                                                                                            	                                            	                                                                    }
	        	                                                                                                                            	                                            	                                                                            .cachedIn(viewModelScope)

	        	                                                                                                                            	                                            	                                                                                fun toggleSelection(id: Long) {
	        	                                                                                                                            	                                            	                                                                                	        _selectedIds.update { current ->
	        	                                                                                                                            	                                            	                                                                                	                    if (current.contains(id)) current - id else current + id
	        	                                                                                                                            	                                            	                                                                                	                            }
	        	                                                                                                                            	                                            	                                                                                	                                    if (_selectedIds.value.isEmpty()) {
	        	                                                                                                                            	                                            	                                                                                	                                    	            _isSelectionMode.value = false
	        	                                                                                                                            	                                            	                                                                                	                                    	                    }
	        	                                                                                                                            	                                            	                                                                                	                                    	                        }

	        	                                                                                                                            	                                            	                                                                                	                                    	                            fun enterSelectionMode(firstId: Long) {
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	        _isSelectionMode.value = true
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                _selectedIds.value = setOf(firstId)
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                    }

	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        fun exitSelectionMode() {
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	        _isSelectionMode.value = false
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                _selectedIds.value = emptySet()
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                    }

	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        fun syncMedia() {
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        viewModelScope.launch {
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        	            repository.syncMediaStore()
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        	                    }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        	                        }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        	                        }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        	        }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        	                        }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            	                        }
	        	                                                                                                                            	                                            	                                                                                	                                    	                            }
	        	                                                                                                                            	                                            	                                                                                	                                    }
	        	                                                                                                                            	                                            	                                                                                }
	        	                                                                                                                            	                                            }
	        	                                                                                                                            }}}
	        }
}
