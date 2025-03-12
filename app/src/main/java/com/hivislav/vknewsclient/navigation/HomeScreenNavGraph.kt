package com.hivislav.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
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
                navArgument(Screen.KEY_FEED_POST_ID) {
                    type = NavType.IntType
                }
            )
        ) {
            val feedPostId = it.arguments?.getInt(Screen.KEY_FEED_POST_ID) ?: 0
            val feedPostContent = it.arguments?.getString(Screen.KEY_FEED_POST_CONTENT) ?: ""
            commentsScreenContent(
                FeedPost(
                    id = feedPostId,
                    contentText = feedPostContent
                )
            )
        }
    }
}
