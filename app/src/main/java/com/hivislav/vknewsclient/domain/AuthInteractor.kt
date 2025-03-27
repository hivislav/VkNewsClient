package com.hivislav.vknewsclient.domain

import kotlinx.coroutines.flow.Flow

interface AuthInteractor {
    fun getToken(): Flow<String?>
    suspend fun updateToken(token: String)
}
