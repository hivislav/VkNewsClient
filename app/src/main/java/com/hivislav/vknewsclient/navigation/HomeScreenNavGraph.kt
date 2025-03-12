package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hivislav.vknewsclient.domain.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    navigation(
        startDestination = Screen.NewsFeedScreen.screenName,
        route = Screen.Home.screenName
    ) {
        composable(route = Screen.NewsFeedScreen.screenName) {
            newsFeedScreenContent()
        }
        composable(
            route = Screen.Comments.screenName,
            arguments = listOf(
                navArgument(Screen.KEY_FEED_POST) {
                    type = FeedPost.NavigationType
                }
            )
        ) {
            val feedPost = it.arguments?.getParcelable(Screen.KEY_FEED_POST, FeedPost::class.java) ?: throw Exception("error get parcelable")
            commentsScreenContent(feedPost)
        }
    }
}
