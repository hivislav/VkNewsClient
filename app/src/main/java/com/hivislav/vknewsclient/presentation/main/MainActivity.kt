package com.hivislav.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hivislav.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkNewsClientTheme {
                val mainViewModel: MainViewModel = viewModel()
                val authState = mainViewModel.stateAuthScreen.observeAsState(AuthState.Initial)
                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }

                    else -> {}
                }
            }
        }
    }
}
