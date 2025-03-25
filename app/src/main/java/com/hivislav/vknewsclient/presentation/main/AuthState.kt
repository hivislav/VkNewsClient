package com.hivislav.vknewsclient.presentation.main

import com.vk.id.AccessToken

sealed class AuthState {
    data class Authorized(val token: AccessToken) : AuthState()
    data class NotAuthorized(val message: String) : AuthState()
    data object Initial : AuthState()
}
