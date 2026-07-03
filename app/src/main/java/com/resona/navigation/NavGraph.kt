package com.resona.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.resona.presentation.common.MiniPlayer
import com.resona.presentation.library.LibraryScreen
import com.resona.presentation.library.LibraryViewModel
import com.resona.presentation.player.PlayerScreen
import com.resona.presentation.player.PlayerViewModel
import com.resona.presentation.settings.SettingsScreen
import com.resona.presentation.splash.SplashScreen

private sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomTab("home", "Home", Icons.Filled.Home)
    object Settings : BottomTab("settings", "Settings", Icons.Filled.Settings)
}

private val bottomTabs = listOf(BottomTab.Home, BottomTab.Settings)

@Composable
fun NavGraph(playerViewModel: PlayerViewModel) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val playerState by playerViewModel.state.collectAsStateWithLifecycle()

    val showChrome = currentRoute == "home" || currentRoute == "settings"

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (showChrome) {
                Column {
                    if (playerState.currentSong != null) {
                        MiniPlayer(
                            state = playerState,
                            onPlayPause = playerViewModel::togglePlayPause,
                            onSkipNext = playerViewModel::skipNext,
                            onClick = { navController.navigate("player") }
                        )
                    }
                    ResonaBottomBar(
                        currentRoute = currentRoute,
                        navController = navController
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            enterTransition = { fadeIn(tween(300)) },
            exitTransition = { fadeOut(tween(300)) },
            popEnterTransition = { fadeIn(tween(300)) },
            popExitTransition = { fadeOut(tween(300)) },
        ) {
            // Splash — manages its own fade-out, so we use instant exit here
            composable(
                route = "splash",
                exitTransition = { fadeOut(tween(0)) }
            ) {
                SplashScreen(
                    onComplete = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                val libraryViewModel: LibraryViewModel = hiltViewModel()
                LibraryScreen(
                    libraryViewModel = libraryViewModel,
                    playerViewModel = playerViewModel,
                    bottomPadding = padding,
                    onNavigateToPlayer = { navController.navigate("player") }
                )
            }

            composable("settings") {
                SettingsScreen(bottomPadding = padding)
            }

            composable("player") {
                PlayerScreen(
                    playerViewModel = playerViewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
private fun ResonaBottomBar(currentRoute: String?, navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        bottomTabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(tab.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = tab.icon, contentDescription = tab.label)
                },
                label = {
                    Text(tab.label, style = MaterialTheme.typography.labelSmall)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )
        }
    }
}
