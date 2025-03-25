package com.hivislav.vknewsclient.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            _stateAuthScreen.value = AuthState.Authorized(accessToken)
        }

        override fun onFail(fail: VKIDAuthFail) {
            _stateAuthScreen.value = AuthState.NotAuthorized(fail.description)
        }
    }

    init {
        viewModelScope.launch {
            VKID.instance.authorize(
                callback = vkAuthCallback,
                params = VKIDAuthParams {

                }
            )
        }
    }

    private val _stateAuthScreen = MutableLiveData<AuthState>(AuthState.Initial)
    val stateAuthScreen: LiveData<AuthState> = _stateAuthScreen
}
