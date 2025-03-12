package com.hivislav.vknewsclient.navigation

import com.hivislav.vknewsclient.domain.FeedPost

sealed class Screen(val screenName: String) {

    object NewsFeedScreen : Screen(NEWS_FEED_SCREEN)
    object FavouriteScreen : Screen(FAVOURITE_SCREEN)
    object ProfileScreen : Screen(PROFILE_SCREEN)
    object Home : Screen(HOME_SCREEN)
    object Comments : Screen(COMMENTS_SCREEN) {

        private const val SCREEN_FOR_ARGS = "comments_screen"

        fun getScreenWithArgs(feedPost: FeedPost): String {
            return "$SCREEN_FOR_ARGS/${feedPost.id}/${feedPost.contentText}"
        }
    }

    companion object {

        const val KEY_FEED_POST_ID = "feed_post_id"
        const val KEY_FEED_POST_CONTENT = "feed_post_content"

        private const val HOME_SCREEN = "home_screen"
        private const val COMMENTS_SCREEN = "comments_screen/{$KEY_FEED_POST_ID}/{$KEY_FEED_POST_CONTENT}"
        private const val NEWS_FEED_SCREEN = "news_feed_screen"

        private const val FAVOURITE_SCREEN = "favourite_screen"
        private const val PROFILE_SCREEN = "profile_screen"
    }
}
