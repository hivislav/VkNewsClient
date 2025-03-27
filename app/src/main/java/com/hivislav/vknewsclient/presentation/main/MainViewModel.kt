package com.hivislav.vknewsclient.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.AuthInteractorImpl
import com.hivislav.vknewsclient.domain.AuthInteractor
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.launch

class MainViewModel(context: Context) : ViewModel() {

    private val authInteractor: AuthInteractor = AuthInteractorImpl(context = context)

    private val vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            viewModelScope.launch {
                authInteractor.updateToken(token = accessToken.token)
            }
            _stateAuthScreen.value = AuthState.Authorized(accessToken.token)
        }

        override fun onFail(fail: VKIDAuthFail) {
            _stateAuthScreen.value = AuthState.NotAuthorized(fail.description)
        }
    }

    init {
        viewModelScope.launch {
            authInteractor.getToken().collect { token ->
                if (!token.isNullOrBlank()) {
                    val refreshToken = VKID.instance.refreshToken?.token ?: ""
                    authInteractor.updateToken(token = refreshToken)
                    _stateAuthScreen.value = AuthState.Authorized(token = refreshToken)
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

    private val _stateAuthScreen = MutableLiveData<AuthState>(AuthState.Initial)
    val stateAuthScreen: LiveData<AuthState> = _stateAuthScreen

    companion object {
        private const val VK_SCOPE_WALL = "wall"
        private const val VK_SCOPE_FRIENDS = "friends"
    }
}
