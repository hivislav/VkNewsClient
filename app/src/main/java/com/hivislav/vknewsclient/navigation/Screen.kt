package com.hivislav.vknewsclient.navigation

sealed class Screen(val route: String) {

    object HomeScreen : Screen(HOME_SCREEN)
    object FavouriteScreen : Screen(FAVOURITE_SCREEN)
    object ProfileScreen : Screen(PROFILE_SCREEN)

    private companion object {
        private const val HOME_SCREEN = "home_screen"
        private const val FAVOURITE_SCREEN = "favourite_screen"
        private const val PROFILE_SCREEN = "profile_screen"
    }
}

