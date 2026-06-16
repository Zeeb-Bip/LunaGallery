package com.zeeb.luna.ui.screens.library

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.zeeb.luna.ui.components.MediaGridItem
import com.zeeb.luna.ui.components.SelectionToolbar
import com.zeeb.luna.util.FormatUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    onMediaClick: (Long) -> Unit,
    viewModel: LibraryViewModel = viewModel()
) {
    val mediaItems = viewModel.mediaPaged.collectAsLazyPagingItems()
    val selectedIds by viewModel.selectedIds.collectAsState()
    val isSelectionMode = selectedIds.isNotEmpty()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    val columns = if (isLandscape) 5 else 3
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollTop by remember {
        derivedStateOf { gridState.firstVisibleItemIndex > 10 }
    }
    val view = LocalView.current

    LaunchedEffect(Unit) {
        viewModel.syncMedia()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Library",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            SelectionToolbar(
                selectedCount = selectedIds.size,
                onShare = { /* TODO: share */ },
                onDelete = { viewModel.deleteSelected() },
                onFavorite = { viewModel.favoriteSelected() },
                onCancel = { viewModel.clearSelection() }
            )
        },
        floatingActionButton = {
            if (showScrollTop) {
                SmallFloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    }
                ) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll ke atas")
                }
            }
        }
    ) { padding ->
        if (mediaItems.itemCount == 0) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Tidak ada foto atau video",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                state = gridState,
                contentPadding = PaddingValues(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding() + 80.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Date headers
                var lastHeader = ""
                items(
                    count = mediaItems.itemCount,
                    key = { index -> mediaItems.peek(index)?.id ?: index }
                ) { index ->
                    val item = mediaItems[index] ?: return@items
                    val header = FormatUtil.formatDateHeader(item.dateTaken)

                    if (header != lastHeader) {
                        lastHeader = header
                    }

                    MediaGridItem(
                        item = item,
                        isSelected = selectedIds.contains(item.id),
                        isSelectionMode = isSelectionMode,
                        onClick = {
                            if (isSelectionMode) {
                                viewModel.toggleSelection(item.id)
                            } else {
                                onMediaClick(item.id)
                            }
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
}
