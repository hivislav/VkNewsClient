package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation


fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable () -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeedScreen.screenName,
        route = Screen.Home.screenName
    ) {
        composable(route = Screen.NewsFeedScreen.screenName) {
            newsFeedScreenContent()
        }
        composable(route = Screen.Comments.screenName) {
            commentsScreenContent()
        }
    }
}
