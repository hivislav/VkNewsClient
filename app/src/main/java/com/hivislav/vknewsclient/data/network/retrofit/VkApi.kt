package com.hivislav.vknewsclient.data.network.retrofit

import com.hivislav.vknewsclient.data.network.model.NewsFeedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("newsfeed.get?v=5.199")
    suspend fun fetchNewsFeed(
        @Query("access_token") token: String,
        @Query("filters") filters: String = "post"
    ): NewsFeedResponse
}
