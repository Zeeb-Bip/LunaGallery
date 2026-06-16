package com.zeeb.luna.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var gridColumns by remember { mutableIntStateOf(3) }
    var showVideoDuration by remember { mutableStateOf(true) }
    var showFileName by remember { mutableStateOf(false) }
    var trashDays by remember { mutableIntStateOf(30) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 8.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text("Tampilan", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text("Kolom Grid: $gridColumns", fontWeight = FontWeight.Medium)
                        Slider(value = gridColumns.toFloat(), onValueChange = { gridColumns = it.toInt() }, valueRange = 2f..5f, steps = 2)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tampilkan durasi video")
                            Switch(checked = showVideoDuration, onCheckedChange = { showVideoDuration = it })
                        }
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tampilkan nama file di grid")
                            Switch(checked = showFileName, onCheckedChange = { showFileName = it })
                        }
                    }
                }
            }
            item {
                Text("Sampah", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                Card {
                    Column(Modifier.padding(16.dp)) {
                        Text("Hapus otomatis setelah: $trashDays hari", fontWeight = FontWeight.Medium)
                        Slider(value = trashDays.toFloat(), onValueChange = { trashDays = it.toInt() }, valueRange = 7f..60f, steps = 3)
                        Text("Pilihan: 7, 14, 30, 60 hari", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            item {
                Text("Tentang", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(vertical = 8.dp))
            }
            item {
                Card {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Aplikasi"); Text("Luna Gallery", color = MaterialTheme.colorScheme.secondary) }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Versi"); Text("1.0.0", color = MaterialTheme.colorScheme.secondary) }
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) { Text("Package"); Text("com.zeeb.luna", color = MaterialTheme.colorScheme.secondary) }
                    }
                }
            }
        }
    }
}
