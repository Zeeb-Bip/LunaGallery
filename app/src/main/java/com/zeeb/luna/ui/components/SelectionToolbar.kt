package com.zeeb.luna.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionToolbar(
	    selectedCount: Int,
	        onCancel: () -> Unit,
	            onShare: () -> Unit,
	                onDelete: () -> Unit,
	                    onFavorite: () -> Unit
	                    ) {
	                    	    TopAppBar(
	                    	    	        title = { Text("$selectedCount Terpilih") },
	                    	    	                navigationIcon = {
	                    	    	                	            IconButton(onClick = onCancel) {
	                    	    	                	            	                Icon(Icons.Default.Close, contentDescription = "Batal")
	                    	    	                	            	                            }
	                    	    	                	            	                                    },
	                    	    	                	            	                                            actions = {
	                    	    	                	            	                                            	            IconButton(onClick = onShare) { Icon(Icons.Default.Share, "Share") }
	                    	    	                	            	                                            	                        IconButton(onClick = onFavorite) { Icon(Icons.Default.FavoriteBorder, "Favorite") }
	                    	    	                	            	                                            	                                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Hapus") }
	                    	    	                	            	                                            	                                            },
	                    	    	                	            	                                            	                                                    colors = TopAppBarDefaults.topAppBarColors(
	                    	    	                	            	                                            	                                                    	            containerColor = MaterialTheme.colorScheme.surface
	                    	    	                	            	                                            	                                                    	                    )
	                    	    	                	            	                                            	                                                    	                        )
	                    	    	                	            	                                            	                                                    	                        }
	                    	    	                	            	                                            	                                                    )
	                    	    	                	            	                                            }
	                    	    	                	            }
	                    	    	                }
	                    	    )
	                    }
)
