package com.zeeb.luna.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.ExifData
import com.zeeb.luna.util.ExifUtil
import com.zeeb.luna.util.FormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExifPanel(
    item: MediaItem,
    exifData: ExifData?,
    locationName: String?,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Informasi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // File info
            ExifSection(title = "File") {
                ExifRow(icon = Icons.Default.InsertDriveFile, label = "Nama", value = item.name)
                ExifRow(icon = Icons.Default.Storage, label = "Ukuran", value = FormatUtil.formatFileSize(item.size))
                ExifRow(icon = Icons.Default.AspectRatio, label = "Resolusi", value = FormatUtil.formatResolution(item.width, item.height))
                ExifRow(icon = Icons.Default.Code, label = "Format", value = item.mimeType)
                ExifRow(icon = Icons.Default.CalendarToday, label = "Tanggal", value = FormatUtil.formatDate(item.dateTaken))
                ExifRow(icon = Icons.Default.Folder, label = "Lokasi File", value = item.path)
            }

            if (exifData != null && !item.isVideo) {
                Spacer(Modifier.height(12.dp))

                // Camera info
                if (exifData.make != null || exifData.model != null) {
                    ExifSection(title = "Kamera") {
                        exifData.make?.let { ExifRow(icon = Icons.Default.Camera, label = "Merek", value = it) }
                        exifData.model?.let { ExifRow(icon = Icons.Default.CameraAlt, label = "Model", value = it) }
                        exifData.focalLength?.let {
                            ExifRow(icon = Icons.Default.CenterFocusStrong, label = "Focal Length", value = ExifUtil.formatFocalLength(it))
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Exposure info
                if (exifData.fNumber != null || exifData.exposureTime != null || exifData.isoSpeed != null) {
                    ExifSection(title = "Eksposur") {
                        exifData.fNumber?.let { ExifRow(icon = Icons.Default.Lens, label = "Aperture", value = "f/$it") }
                        exifData.exposureTime?.let {
                            ExifRow(icon = Icons.Default.Timer, label = "Shutter Speed", value = ExifUtil.formatExposureTime(it))
                        }
                        exifData.isoSpeed?.let { ExifRow(icon = Icons.Default.Iso, label = "ISO", value = it) }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // GPS info
                if (exifData.gpsLatitude != null && exifData.gpsLongitude != null) {
                    ExifSection(title = "Lokasi") {
                        ExifRow(
                            icon = Icons.Default.LocationOn,
                            label = "Koordinat",
                            value = "%.6f, %.6f".format(exifData.gpsLatitude, exifData.gpsLongitude)
                        )
                        locationName?.let {
                            ExifRow(icon = Icons.Default.Place, label = "Nama Lokasi", value = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExifSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            content()
        }
    }
}

@Composable
private fun ExifRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp).padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.width(90.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.weight(1f)
        )
    }
}
