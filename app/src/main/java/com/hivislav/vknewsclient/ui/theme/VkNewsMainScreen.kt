package com.hivislav.vknewsclient.ui.theme

import androidx.compose.foundation.clickable
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hivislav.vknewsclient.MainViewModel
import com.hivislav.vknewsclient.navigation.AppNavGraph
import com.hivislav.vknewsclient.navigation.rememberNavigationState

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navigationState = rememberNavigationState()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary.copy(blue = 0.9f),
        bottomBar = {
            BottomNavigation {
                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favourite,
                    NavigationItem.Profile
                )
                items.forEach { item ->
                    val backStackEntry by navigationState.navController.currentBackStackEntryAsState()
                    val currentRoute = backStackEntry?.destination?.route

                    BottomNavigationItem(
                        selected = currentRoute == item.screen.route,
                        onClick = { navigationState.navigateTo(item.screen.route)},
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
        AppNavGraph(
            navHostController = navigationState.navController,
            homeScreenContent = { HomeScreen(viewModel = viewModel, paddingValues = innerPadding) },
            favouriteScreenContent = { TextCounter(text = "Favourite", color = Color.Black) },
            profileScreenContent = { TextCounter(text = "Profile", color = Color.Black) }
        )
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
