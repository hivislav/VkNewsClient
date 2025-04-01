package com.hivislav.vknewsclient.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.AppDataStore
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {

    private val appDataStore: AppDataStore = AppDataStore(context = context)
    private val _stateAuthScreen = MutableLiveData<AuthState>(AuthState.Initial)
    val stateAuthScreen: LiveData<AuthState> = _stateAuthScreen

    private val isTokenUpdated = MutableLiveData(false)

    private val vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            _stateAuthScreen.value = AuthState.Authorized(accessToken.token)
            isTokenUpdated.value = true
            viewModelScope.launch {
                appDataStore.updateToken(token = accessToken.token)
            }
        }

        override fun onFail(fail: VKIDAuthFail) {
            _stateAuthScreen.value = AuthState.NotAuthorized(fail.description)
        }
    }

    private val vkRefreshTokenCallback = object : VKIDRefreshTokenCallback {
        override fun onSuccess(token: AccessToken) {
            _stateAuthScreen.value = AuthState.Authorized(token = token.token)
            isTokenUpdated.value = true
            viewModelScope.launch {
                appDataStore.updateToken(token = token.token)
            }
        }

        override fun onFail(fail: VKIDRefreshTokenFail) {
            // TODO
        }
    }

    init {
        viewModelScope.launch {
            appDataStore.getToken().collect { token ->
                if (!token.isNullOrBlank()) {
                    if (isTokenUpdated.value != true) {
                        VKID.instance.refreshToken(
                            callback = vkRefreshTokenCallback
                        )
                    }
                } else {
                    VKID.instance.authorize(
                        callback = vkAuthCallback,
                        params = VKIDAuthParams {
                            scopes = setOf(VK_SCOPE_WALL, VK_SCOPE_FRIENDS)
                        }
                    )
                }
            }
        }
    }

    companion object {
        private const val VK_SCOPE_WALL = "wall"
        private const val VK_SCOPE_FRIENDS = "friends"
    }
}
