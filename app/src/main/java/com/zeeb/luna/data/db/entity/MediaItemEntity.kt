package com.zeeb.luna.domain.model

import android.net.Uri

data class MediaItem(
	    val id: Long,
	        val uri: Uri,
	            val path: String,
	                val name: String,
	                    val mimeType: String,
	                        val size: Long,
	                            val width: Int,
	                                val height: Int,
	                                    val duration: Long,
	                                        val dateAdded: Long,
	                                            val dateTaken: Long,
	                                                val bucketId: Long,
	                                                    val bucketName: String,
	                                                        val isFavorite: Boolean,
	                                                            val isDeleted: Boolean,
	                                                                val deletedAt: Long?,
	                                                                    val mediaType: Int // MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE atau VIDEO
	                                                                    ) {
	                                                                    	    val isVideo: Boolean get() = mediaType == 3 // MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
	                                                                    	        val isRaw: Boolean get() = mimeType.contains("raw") || mimeType.contains("x-adobe-dng") || 
	                                                                    	                    mimeType.contains("x-canon-cr2") || mimeType.contains("x-nikon-nef") || 
	                                                                    	                                mimeType.contains("x-sony-arw")
	                                                                    	                                }
	                                                                    }
)
