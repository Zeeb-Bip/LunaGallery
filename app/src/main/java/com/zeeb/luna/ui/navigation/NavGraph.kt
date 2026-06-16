package com.zeeb.luna.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.zeeb.luna.ui.screens.albums.AlbumDetailScreen
import com.zeeb.luna.ui.screens.albums.AlbumsScreen
import com.zeeb.luna.ui.screens.library.LibraryScreen
import com.zeeb.luna.ui.screens.search.SearchScreen
import com.zeeb.luna.ui.screens.settings.SettingsScreen
import com.zeeb.luna.ui.screens.viewer.MediaViewerScreen

sealed class Screen(val route: String) {
    object Library : Screen("library")
    object Albums : Screen("albums")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object AlbumDetail : Screen("album/{bucketId}/{albumName}") {
        fun createRoute(bucketId: Long, albumName: String) = "album/$bucketId/$albumName"
    }
    object Viewer : Screen("viewer/{mediaId}/{source}") {
        fun createRoute(mediaId: Long, source: String = "library") = "viewer/$mediaId/$source"
    }
}

@Composable
fun LunaNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route,
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() }
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(
                onMediaClick = { mediaId ->
                    navController.navigate(Screen.Viewer.createRoute(mediaId, "library"))
                }
            )
        }

        composable(Screen.Albums.route) {
            AlbumsScreen(
                onAlbumClick = { bucketId, albumName ->
                    navController.navigate(Screen.AlbumDetail.createRoute(bucketId, albumName))
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onMediaClick = { mediaId ->
                    navController.navigate(Screen.Viewer.createRoute(mediaId, "search"))
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Screen.AlbumDetail.route,
            arguments = listOf(
                navArgument("bucketId") { type = NavType.LongType },
                navArgument("albumName") { type = NavType.StringType }
            )
        ) { backStack ->
            val bucketId = backStack.arguments?.getLong("bucketId") ?: return@composable
            val albumName = backStack.arguments?.getString("albumName") ?: ""
            AlbumDetailScreen(
                bucketId = bucketId,
                albumName = albumName,
                onMediaClick = { mediaId ->
                    navController.navigate(Screen.Viewer.createRoute(mediaId, "album_$bucketId"))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Viewer.route,
            arguments = listOf(
                navArgument("mediaId") { type = NavType.LongType },
                navArgument("source") { type = NavType.StringType }
            )
        ) { backStack ->
            val mediaId = backStack.arguments?.getLong("mediaId") ?: return@composable
            val source = backStack.arguments?.getString("source") ?: "library"
            MediaViewerScreen(
                initialMediaId = mediaId,
                source = source,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
