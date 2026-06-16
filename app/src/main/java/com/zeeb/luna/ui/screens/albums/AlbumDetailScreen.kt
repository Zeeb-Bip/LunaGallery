package com.zeeb.luna.ui.screens.albums

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.zeeb.luna.data.db.LunaDatabase
import com.zeeb.luna.data.repository.MediaRepository
import com.zeeb.luna.ui.components.MediaGridItem
import com.zeeb.luna.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun AlbumDetailScreen(
	    bucketId: Long,
	        albumName: String,
	            navController: NavController,
	                sharedTransitionScope: SharedTransitionScope,
	                    animatedVisibilityScope: AnimatedVisibilityScope
	                    ) {
	                    	    val context = LocalContext.current
	                    	        val repository = remember { MediaRepository(context, LunaDatabase.getDatabase(context)) }
	                    	            val pagedItems = remember(bucketId) {
	                    	            	        repository.getMediaByAlbumPaged(bucketId)
	                    	            	            }.collectAsLazyPagingItems()

	                    	            	                Scaffold(
	                    	            	                	        topBar = {
	                    	            	                	        	            TopAppBar(
	                    	            	                	        	            	                title = { Text(albumName) },
	                    	            	                	        	            	                                navigationIcon = {
	                    	            	                	        	            	                                	                    IconButton(onClick = { navController.popBackStack() }) {
	                    	            	                	        	            	                                	                    	                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
	                    	            	                	        	            	                                	                    	                                            }
	                    	            	                	        	            	                                	                    	                                                            }
	                    	            	                	        	            	                                	                    	                                                                        )
	                    	            	                	        	            	                                	                    	                                                                                }
	                    	            	                	        	            	                                	                    	                                                                                    ) { padding ->
	                    	            	                	        	            	                                	                    	                                                                                            LazyVerticalGrid(
	                    	            	                	        	            	                                	                    	                                                                                            	            columns = GridCells.Fixed(3),
	                    	            	                	        	            	                                	                    	                                                                                            	                        modifier = Modifier.fillMaxSize().padding(padding),
	                    	            	                	        	            	                                	                    	                                                                                            	                                    contentPadding = PaddingValues(1.dp)
	                    	            	                	        	            	                                	                    	                                                                                            	                                            ) {
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	            items(pagedItems.itemCount) { index ->
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                            val media = pagedItems[index]
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            if (media != null) {
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    MediaGridItem(
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                        media = media,
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                isSelected = false,
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                        isSelectionMode = false,
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                onLongClick = {},
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        onClick = {
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                            navController.navigate(Screen.Viewer.createRoute(media.id))
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                    },
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                            sharedTransitionScope = sharedTransitionScope,
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                    animatedVisibilityScope = animatedVisibilityScope
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                        )
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                                        }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                                                    }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                                                            }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                                                                }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        	                                                                                                                                                                }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    	                                                                                                                        }
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            	                    )
	                    	            	                	        	            	                                	                    	                                                                                            	                                            	                                            }}
	                    	            	                	        	            	                                	                    	                                                                                            	                                            }
	                    	            	                	        	            	                                	                    	                                                                                            )}
	                    	            	                	        	            	                                	                    }
	                    	            	                	        	            	                                }
	                    	            	                	        	            )
	                    	            	                	        }
	                    	            	                )
	                    	            }
	                    }
)
