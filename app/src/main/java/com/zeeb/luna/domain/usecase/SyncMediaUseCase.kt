package com.zeeb.luna.domain.usecase

import com.zeeb.luna.data.repository.MediaRepository

class SyncMediaUseCase(private val repository: MediaRepository) {
    suspend operator fun invoke() = repository.syncMediaStore()
}
