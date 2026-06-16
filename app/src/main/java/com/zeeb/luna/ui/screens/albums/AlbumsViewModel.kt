package com.zeeb.luna.ui.screens.albums

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.Album
import kotlinx.coroutines.flow.*

data class AlbumsUiState(
	    val smartAlbums: List<Album> = emptyList(),
	        val userAlbums: List<Album> = emptyList()
	        )

	        class AlbumsViewModel(private val repository: MediaRepository) : ViewModel() {

	        	    val uiState: StateFlow<AlbumsUiState> = repository.getAlbums()
	        	            .map { albums ->
	        	                        // Di sini kita bisa memfilter atau menambahkan Smart Albums secara manual
	        	                                    // Jika datanya belum ada di DB, kita buat placeholder atau query khusus
	        	                                                AlbumsUiState(
	        	                                                	                smartAlbums = listOf(
	        	                                                	                	                    Album(-1, "Favorites ❤️", "", 0, 0, false),
	        	                                                	                	                                        Album(-2, "Videos 🎬", "", 0, 0, false),
	        	                                                	                	                                                            Album(-3, "Recently Deleted 🗑️", "", 0, 0, false)
	        	                                                	                	                                                                            ),
	        	                                                	                	                                                                                            userAlbums = albums
	        	                                                	                	                                                                                                        )
	        	                                                	                	                                                                                                                }
	        	                                                	                	                                                                                                                        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AlbumsUiState())
	        	                                                	                	                                                                                                                        }
	        	                                                	                )
	        	                                                )}
	        }
)
