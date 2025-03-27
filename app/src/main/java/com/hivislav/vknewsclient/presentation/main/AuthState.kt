package com.hivislav.vknewsclient.presentation.main

sealed class AuthState {
    data class Authorized(val token: String) : AuthState()
    data class NotAuthorized(val message: String) : AuthState()
    data object Initial : AuthState()
}
