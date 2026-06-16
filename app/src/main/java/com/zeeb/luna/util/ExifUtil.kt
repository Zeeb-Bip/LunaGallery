package com.zeeb.luna.util

import android.content.Context
import android.media.ExifInterface
import android.net.Uri
import java.io.InputStream

data class ExifMetadata(
	    val make: String? = null,
	        val model: String? = null,
	            val aperture: String? = null,
	                val shutterSpeed: String? = null,
	                    val iso: String? = null,
	                        val focalLength: String? = null,
	                            val latLong: DoubleArray? = null
	                            )

	                            object ExifUtil {
	                            	    fun getMetadata(context: Context, uri: Uri): ExifMetadata {
	                            	    	        return try {
	                            	    	        	            context.contentResolver.openInputStream(uri)?.use { inputStream ->
	                            	    	        	                            val exif = ExifInterface(inputStream)
	                            	    	        	                                            ExifMetadata(
	                            	    	        	                                            	                    make = exif.getAttribute(ExifInterface.TAG_MAKE),
	                            	    	        	                                            	                                        model = exif.getAttribute(ExifInterface.TAG_MODEL),
	                            	    	        	                                            	                                                            aperture = exif.getAttribute(ExifInterface.TAG_F_NUMBER),
	                            	    	        	                                            	                                                                                shutterSpeed = exif.getAttribute(ExifInterface.TAG_EXPOSURE_TIME),
	                            	    	        	                                            	                                                                                                    iso = exif.getAttribute(ExifInterface.TAG_ISO_SPEED_RATINGS),
	                            	    	        	                                            	                                                                                                                        focalLength = exif.getAttribute(ExifInterface.TAG_FOCAL_LENGTH),
	                            	    	        	                                            	                                                                                                                                            latLong = exif.latLong
	                            	    	        	                                            	                                                                                                                                                            )
	                            	    	        	                                            	                                                                                                                                                                        } ?: ExifMetadata()
	                            	    	        	                                            	                                                                                                                                                                                } catch (e: Exception) {
	                            	    	        	                                            	                                                                                                                                                                                	            ExifMetadata()
	                            	    	        	                                            	                                                                                                                                                                                	                    }
	                            	    	        	                                            	                                                                                                                                                                                	                        }
	                            	    	        	                                            	                                                                                                                                                                                	                        }
	                            	    	        	                                            	                                                                                                                                                                                }
	                            	    	        	                                            )}
	                            	    	        }
	                            	    }
	                            }
)
