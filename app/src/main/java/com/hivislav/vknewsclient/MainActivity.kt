package com.hivislav.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.SideEffect
import com.hivislav.vknewsclient.ui.theme.MainScreen
import com.hivislav.vknewsclient.ui.theme.VkNewsClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VkNewsClientTheme {
                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract(),
                    onResult = { result ->
                        when(result) {
                            is VKAuthenticationResult.Success -> {
                                Log.d("@@@@", "SUCCESS")
                            }
                            is VKAuthenticationResult.Failed -> {
                                Log.d("@@@@", "FAILED")
                            }
                        }
                    }
                )

                SideEffect {
                    launcher.launch(arrayListOf(VKScope.EMAIL))
                }

                MainScreen()
            }
        }
    }
}
