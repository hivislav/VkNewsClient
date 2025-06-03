package com.hivislav.vknewsclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hivislav.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkNewsClientTheme {
                val mainViewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(context = this)
                )
                val authState = mainViewModel.stateAuthScreen.collectAsState()
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
