package com.hivislav.vknewsclient.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hivislav.vknewsclient.navigation.AppNavGraph
import com.hivislav.vknewsclient.navigation.rememberNavigationState
import com.hivislav.vknewsclient.presentation.comments.CommentsScreen
import com.hivislav.vknewsclient.presentation.news.NewsFeedScreen

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary.copy(blue = 0.9f),
        bottomBar = {
            BottomNavigation {
                val backStackEntry by navigationState.navController.currentBackStackEntryAsState()

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favourite,
                    NavigationItem.Profile
                )
                items.forEach { item ->
                    val selected = backStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.screenName
                    } ?: false

                    BottomNavigationItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navigationState.navigateTo(item.screen.screenName)
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        selectedContentColor = MaterialTheme.colors.onPrimary,
                        unselectedContentColor = MaterialTheme.colors.onSecondary
                    )
                }
            }
        }
    ) { innerPadding ->

        Column {
            AppNavGraph(
                navHostController = navigationState.navController,
                newsFeedScreenContent = {
                    NewsFeedScreen(
                        paddingValues = innerPadding,
                        onCommentClick = {
                            navigationState.navigateToComments(feedPost = it)
                        }
                    )
                },
                commentsScreenContent = { feedPost ->
                    CommentsScreen(
                        feedPost = feedPost,
                        onBackPressed = {
                            navigationState.navController.popBackStack()
                        }
                    )
                },
                favouriteScreenContent = { TextCounter(text = "Favourite", color = Color.Black) },
                profileScreenContent = { TextCounter(text = "Profile", color = Color.Black) }
            )
        }
    }
}

@Composable
private fun TextCounter(text: String, color: Color) {
    var textCounter by rememberSaveable {
        mutableStateOf(0)
    }

    Text(
        modifier = Modifier.clickable { textCounter++ },
        text = "$text $textCounter",
        color = color
    )
}
