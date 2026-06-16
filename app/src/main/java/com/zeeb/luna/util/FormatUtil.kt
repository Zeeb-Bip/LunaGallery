package com.zeeb.luna.util

import java.text.SimpleDateFormat
import java.util.*

object FormatUtil {
    fun formatFileSize(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> "%.1f GB".format(bytes / 1_073_741_824.0)
            bytes >= 1_048_576 -> "%.1f MB".format(bytes / 1_048_576.0)
            bytes >= 1_024 -> "%.1f KB".format(bytes / 1_024.0)
            else -> "$bytes B"
        }
    }

    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, minutes % 60, seconds % 60)
        } else {
            "%d:%02d".format(minutes, seconds % 60)
        }
    }

    fun formatDate(epochMs: Long): String {
        val sdf = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        return sdf.format(Date(epochMs))
    }

    fun formatDateHeader(epochMs: Long): String {
        val cal = Calendar.getInstance()
        val today = Calendar.getInstance()
        cal.timeInMillis = epochMs
        return when {
            cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) -> "Hari ini"
            cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) - 1 -> "Kemarin"
            else -> SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date(epochMs))
        }
    }

    fun formatResolution(width: Int, height: Int): String = "${width} × ${height}"
}
