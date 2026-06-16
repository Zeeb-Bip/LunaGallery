package com.zeeb.luna.ui.screens.viewer

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.ui.components.ExifPanel
import com.zeeb.luna.ui.components.VideoPlayer
import com.zeeb.luna.util.FormatUtil
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaViewerScreen(
    initialMediaId: Long,
    source: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val repository = remember { MediaRepository(context) }
    val viewModel = remember { ViewerViewModel(repository) }

    var showUI by remember { mutableStateOf(true) }
    var showExif by remember { mutableStateOf(false) }
    var currentMedia by remember { mutableStateOf<MediaItem?>(null) }

    val currentExif by viewModel.currentExif.collectAsState()
    val locationName by viewModel.locationName.collectAsState()

    LaunchedEffect(initialMediaId) {
        val media = repository.getById(initialMediaId) // tambahkan fungsi ini di Repository jika belum ada
        currentMedia = media
        if (media != null) {
        }
    }

    LaunchedEffect(showUI) {
        if (showUI) {
            delay(3000)
            showUI = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { showUI = !showUI })
            }
    ) {
        currentMedia?.let { media ->
            if (media.isVideo) {
                VideoPlayer(uri = media.uri)
            } else {
                AsyncImage(
                    model = media.uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // UI Top & Bottom (sama seperti sebelumnya)
            AnimatedVisibility(visible = showUI, enter = slideInVertically() + fadeIn(), exit = slideOutVertically() + fadeOut()) {
                TopAppBar(
                    title = { Text(FormatUtil.formatDate(media.dateTaken)) },
                    navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black.copy(alpha = 0.4f))
                )
            }

            AnimatedVisibility(visible = showUI, modifier = Modifier.align(Alignment.BottomCenter)) {
                BottomAppBar(containerColor = Color.Black.copy(alpha = 0.4f)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        IconButton(onClick = { viewModel.toggleFavorite(media) }) {
                            Icon(if (media.isFavorite) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder, null, tint = if (media.isFavorite) Color.Red else Color.White)
                        }
                        IconButton(onClick = { viewModel.loadExif(context, media); showExif = true }) {
                            Icon(Icons.Outlined.Info, null)
                        }
                        IconButton(onClick = { viewModel.deleteMedia(media) { onBack() } }) {
                            Icon(Icons.Default.Delete, null)
                        }
                    }
                }
            }

            if (showExif) {
                ExifPanel(item = media, exifData = currentExif, locationName = locationName, onDismiss = { showExif = false })
            }
        } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}
