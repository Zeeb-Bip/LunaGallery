package com.zeeb.luna.ui.screens.viewer

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.ExifData
import com.zeeb.luna.util.ExifUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class ViewerViewModel(private val repository: MediaRepository) : ViewModel() {

    private val _currentExif = MutableStateFlow<ExifData?>(null)
    val currentExif: StateFlow<ExifData?> = _currentExif.asStateFlow()

    private val _locationName = MutableStateFlow<String?>(null)
    val locationName: StateFlow<String?> = _locationName.asStateFlow()

    fun loadExif(context: Context, media: MediaItem) {
        viewModelScope.launch(Dispatchers.IO) {
            val exif = ExifUtil.readExif(media.path)
            _currentExif.value = exif

            if (exif.gpsLatitude != null && exif.gpsLongitude != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(exif.gpsLatitude, exif.gpsLongitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        _locationName.value = "${address.locality ?: ""}, ${address.adminArea ?: ""}".trim(',', ' ')
                    }
                } catch (e: Exception) {
                    _locationName.value = null
                }
            } else {
                _locationName.value = null
            }
        }
    }

    fun toggleFavorite(media: MediaItem) {
        viewModelScope.launch {
            repository.toggleFavorite(media.id, !media.isFavorite)
        }
    }

    fun deleteMedia(media: MediaItem, onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.moveToTrash(media.id)
            onDeleted()
        }
    }
}
