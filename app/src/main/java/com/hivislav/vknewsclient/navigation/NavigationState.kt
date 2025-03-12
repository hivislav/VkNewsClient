package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hivislav.vknewsclient.domain.FeedPost

class NavigationState(
    val navController: NavHostController,
) {

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToComments(feedPost: FeedPost) {
        navController.navigate(Screen.Comments.getScreenWithArgs(feedPost = feedPost))
    }
}

@Composable
fun rememberNavigationState(
    navController: NavHostController = rememberNavController(),
): NavigationState {
    return remember {
        NavigationState(navController)
    }
}
