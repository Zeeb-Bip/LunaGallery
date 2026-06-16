package com.zeeb.luna.domain.usecase

import com.zeeb.luna.data.repository.MediaRepository

class GetMediaUseCase(private val repository: MediaRepository) {
    operator fun invoke() = repository.getAllMediaPaged()
}
