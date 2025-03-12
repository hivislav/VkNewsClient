package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hivislav.vknewsclient.domain.FeedPost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.screenName
    ) {
        homeScreenNavGraph(
            newsFeedScreenContent = newsFeedScreenContent,
            commentsScreenContent = commentsScreenContent
        )
        composable(route = Screen.FavouriteScreen.screenName) {
            favouriteScreenContent()
        }
        composable(route = Screen.ProfileScreen.screenName) {
            profileScreenContent()
        }
    }
}
