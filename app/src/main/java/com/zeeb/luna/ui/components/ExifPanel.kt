package com.zeeb.luna.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zeeb.luna.domain.model.MediaItem
import com.zeeb.luna.util.FormatUtil

@Composable
fun ExifPanel(media: MediaItem) {
	    Column(
	    	        modifier = Modifier
	    	                    .fillMaxWidth()
	    	                                .padding(20.dp)
	    	                                    ) {
	    	                                    	        Text(
	    	                                    	        	            text = "Informasi",
	    	                                    	        	                        style = MaterialTheme.typography.titleLarge,
	    	                                    	        	                                    fontWeight = FontWeight.Bold
	    	                                    	        	                                            )
	    	                                    	        	                                                    Spacer(modifier = Modifier.height(16.dp))

	    	                                    	        	                                                                    InfoRow(label = "Nama File", value = media.name)
	    	                                    	        	                                                                            InfoRow(label = "Tanggal", value = FormatUtil.formatDate(media.dateTaken))
	    	                                    	        	                                                                                    InfoRow(label = "Ukuran", value = FormatUtil.formatFileSize(media.size))
	    	                                    	        	                                                                                            InfoRow(label = "Resolusi", value = "${media.width} x ${media.height}")
	    	                                    	        	                                                                                                    InfoRow(label = "Format", value = media.mimeType)
	    	                                    	        	                                                                                                            InfoRow(label = "Path", value = media.path)
	    	                                    	        	                                                                                                                }
	    	                                    	        	                                                                                                                }

	    	                                    	        	                                                                                                                @Composable
	    	                                    	        	                                                                                                                fun InfoRow(label: String, value: String) {
	    	                                    	        	                                                                                                                	    Column(modifier = Modifier.padding(vertical = 8.dp)) {
	    	                                    	        	                                                                                                                	    	        Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
	    	                                    	        	                                                                                                                	    	                Text(text = value, style = MaterialTheme.typography.bodyLarge)
	    	                                    	        	                                                                                                                	    	                    }
	    	                                    	        	                                                                                                                	    	                    }
	    	                                    	        	                                                                                                                	    }
	    	                                    	        	                                                                                                                }
	    	                                    	        )
	    	                                    }
	    )
}
