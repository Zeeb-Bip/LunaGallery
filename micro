package com.zeeb.luna.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zeeb.luna.ui.screens.library.LibraryScreen
import com.zeeb.luna.ui.screens.albums.AlbumsScreen
import com.zeeb.luna.ui.screens.search.SearchScreen
import com.zeeb.luna.ui.screens.viewer.MediaViewerScreen
import com.zeeb.luna.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
	    object Library : Screen("library")
	        object Albums : Screen("albums")
	            object Search : Screen("search")
	                object Viewer : Screen("viewer/{mediaId}") {
	                	        fun createRoute(mediaId: Long) = "viewer/$mediaId"
	                	            }
	                	                object Settings : Screen("settings")
	                	                }

	                	                @OptIn(ExperimentalSharedTransitionApi::class)
	                	                @Composable
	                	                fun LunaNavGraph(navController: NavHostController) {
	                	                	    SharedTransitionLayout {
	                	                	    	        NavHost(
	                	                	    	        	            navController = navController,
	                	                	    	        	                        startDestination = Screen.Library.route
	                	                	    	        	                                ) {
	                	                	    	        	                                	            composable(Screen.Library.route) {
	                	                	    	        	                                	            	                LibraryScreen(
	                	                	    	        	                                	            	                	                    navController = navController,
	                	                	    	        	                                	            	                	                                        sharedTransitionScope = this@SharedTransitionLayout,
	                	                	    	        	                                	            	                	                                                            animatedVisibilityScope = this
	                	                	    	        	                                	            	                	                                                                            )
	                	                	    	        	                                	            	                	                                                                                        }
	                	                	    	        	                                	            	                	                                                                                                    composable(Screen.Albums.route) {
	                	                	    	        	                                	            	                	                                                                                                    	                AlbumsScreen(navController = navController)
	                	                	    	        	                                	            	                	                                                                                                    	                            }
	                	                	    	        	                                	            	                	                                                                                                    	                                        composable(Screen.Search.route) {
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                SearchScreen(navController = navController)
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                            }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        composable(
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                route = Screen.Viewer.route,
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                arguments = listOf(navArgument("mediaId") { type = NavType.LongType })
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                            ) { backStackEntry ->
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                            val mediaId = backStackEntry.arguments?.getLong("mediaId") ?: 0L
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            MediaViewerScreen(
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                    mediaId = mediaId,
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                        navController = navController,
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                            sharedTransitionScope = this@SharedTransitionLayout,
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                animatedVisibilityScope = this
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                )
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                            }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        composable(Screen.Settings.route) {
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        	                SettingsScreen(navController = navController)
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        	                            }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        	                                    }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        	                                        }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        	                                        }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            	                                                                                                                        }
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        	                                                                            )}
	                	                	    	        	                                	            	                	                                                                                                    	                                        	                                        )
	                	                	    	        	                                	            	                	                                                                                                    	                                        }
	                	                	    	        	                                	            	                	                                                                                                    }
	                	                	    	        	                                	            	                )
	                	                	    	        	                                	            }
	                	                	    	        	                                }
	                	                	    	        )
	                	                	    }
	                	                }
	                }
}
