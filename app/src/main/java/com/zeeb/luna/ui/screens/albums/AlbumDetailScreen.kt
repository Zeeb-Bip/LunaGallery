package com.zeeb.luna.ui.screens.albums

import android.app.Application
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.ui.components.MediaGridItem
import com.zeeb.luna.ui.components.SelectionToolbar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlbumDetailViewModel(application: Application, bucketId: Long) : androidx.lifecycle.AndroidViewModel(application) {
    private val repository = MediaRepository(application)
    val mediaPaged = repository.getMediaByBucketPaged(bucketId).cachedIn(viewModelScope)

    private val _selectedIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedIds = _selectedIds.asStateFlow()

    fun toggleSelection(id: Long) {
        _selectedIds.value = _selectedIds.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }
    }

    fun clearSelection() { _selectedIds.value = emptySet() }

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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    bucketId: Long,
    albumName: String,
    onMediaClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: AlbumDetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(
                    context.applicationContext as Application,
                    bucketId
                ) as T
            }
        }
    )
    val mediaItems = viewModel.mediaPaged.collectAsLazyPagingItems()
    val selectedIds by viewModel.selectedIds.collectAsState()
    val isSelectionMode = selectedIds.isNotEmpty()
    val view = LocalView.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(albumName, fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                }
            )
        },
        bottomBar = {
            SelectionToolbar(
                selectedCount = selectedIds.size,
                onShare = {},
                onDelete = { viewModel.deleteSelected() },
                onFavorite = { viewModel.favoriteSelected() },
                onCancel = { viewModel.clearSelection() }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding() + 80.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                count = mediaItems.itemCount,
                key = { index -> mediaItems.peek(index)?.id ?: index }
            ) { index ->
                val item = mediaItems[index] ?: return@items
                MediaGridItem(
                    item = item,
                    isSelected = selectedIds.contains(item.id),
                    isSelectionMode = isSelectionMode,
                    onClick = {
                        if (isSelectionMode) viewModel.toggleSelection(item.id)
                        else onMediaClick(item.id)
                    },
                    onLongClick = {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        viewModel.toggleSelection(item.id)
                    }
                )
            }
        }
    }
}
