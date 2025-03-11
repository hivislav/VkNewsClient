package com.hivislav.vknewsclient.navigation

sealed class Screen(val screenName: String) {

    object NewsFeedScreen : Screen(NEWS_FEED_SCREEN)
    object FavouriteScreen : Screen(FAVOURITE_SCREEN)
    object ProfileScreen : Screen(PROFILE_SCREEN)
    object Home : Screen(HOME_SCREEN)
    object Comments : Screen(COMMENTS_SCREEN)

    private companion object {

        private const val HOME_SCREEN = "home_screen"
        private const val COMMENTS_SCREEN = "comments_screen"
        private const val NEWS_FEED_SCREEN = "news_feed_screen"

        private const val FAVOURITE_SCREEN = "favourite_screen"
        private const val PROFILE_SCREEN = "profile_screen"
    }
}

