package com.zeeb.luna.util

import androidx.exifinterface.media.ExifInterface
import java.io.File

data class ExifData(
    val make: String? = null,
    val model: String? = null,
    val dateTime: String? = null,
    val fNumber: String? = null,
    val exposureTime: String? = null,
    val isoSpeed: String? = null,
    val focalLength: String? = null,
    val gpsLatitude: Double? = null,
    val gpsLongitude: Double? = null,
    val width: Int? = null,
    val height: Int? = null,
    val flash: String? = null,
    val whiteBalance: String? = null
)

object ExifUtil {
    fun readExif(path: String): ExifData {
        return try {
            val exif = ExifInterface(File(path))
            val latLon = FloatArray(2)
            val hasGps = exif.getLatLong(latLon)
            ExifData(
                make = exif.getAttribute(ExifInterface.TAG_MAKE),
                model = exif.getAttribute(ExifInterface.TAG_MODEL),
                dateTime = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                    ?: exif.getAttribute(ExifInterface.TAG_DATETIME),
                fNumber = exif.getAttribute(ExifInterface.TAG_F_NUMBER),
                exposureTime = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME),
                isoSpeed = exif.getAttribute(ExifInterface.TAG_PHOTOGRAPHIC_SENSITIVITY),
                focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH),
                gpsLatitude = if (hasGps) latLon[0].toDouble() else null,
                gpsLongitude = if (hasGps) latLon[1].toDouble() else null,
                width = exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0).takeIf { it > 0 },
                height = exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0).takeIf { it > 0 },
                flash = exif.getAttribute(ExifInterface.TAG_FLASH),
                whiteBalance = exif.getAttribute(ExifInterface.TAG_WHITE_BALANCE)
            )
        } catch (e: Exception) {
            ExifData()
        }
    }

    fun formatExposureTime(raw: String?): String {
        if (raw == null) return "-"
        return try {
            val value = raw.toDouble()
            if (value < 1.0) "1/${(1.0 / value).toInt()}s" else "${value}s"
        } catch (e: Exception) { raw }
    }

    fun formatFocalLength(raw: String?): String {
        if (raw == null) return "-"
        return try {
            val parts = raw.split("/")
            if (parts.size == 2) {
                val mm = parts[0].toDouble() / parts[1].toDouble()
                "%.1fmm".format(mm)
            } else "$raw mm"
        } catch (e: Exception) { raw }
    }
}
