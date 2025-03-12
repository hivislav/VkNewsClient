package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
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
        composable(route = Screen.Comments.screenName) {
            val feedPostId = it.arguments?.getString("feed_post_id")?.toIntOrNull() ?: 0
            commentsScreenContent(FeedPost(id = feedPostId))
        }
    }
}
