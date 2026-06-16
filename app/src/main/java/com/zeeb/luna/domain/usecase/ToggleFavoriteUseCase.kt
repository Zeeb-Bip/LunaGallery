package com.zeeb.luna.domain.usecase

import com.zeeb.luna.data.repository.MediaRepository

class ToggleFavoriteUseCase(private val repository: MediaRepository) {
	    suspend operator fun invoke(id: Long, isFavorite: Boolean) = 
	            repository.toggleFavorite(id, isFavorite)
	            }
}
