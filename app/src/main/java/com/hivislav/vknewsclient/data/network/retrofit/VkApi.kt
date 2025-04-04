package com.hivislav.vknewsclient.data.network.retrofit

import com.hivislav.vknewsclient.data.network.model.CommentsResponse
import com.hivislav.vknewsclient.data.network.model.LikesCountResponse
import com.hivislav.vknewsclient.data.network.model.NewsFeedResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("newsfeed.get?v=5.199")
    suspend fun fetchNewsFeed(
        @Query("access_token") token: String,
        @Query("filters") filters: String = "post",
    ): NewsFeedResponse

    @GET("newsfeed.get?v=5.199")
    suspend fun fetchNewsFeed(
        @Query("access_token") token: String,
        @Query("filters") filters: String = "post",
        @Query("start_from") startFrom: String,
    ): NewsFeedResponse

    @GET("likes.add?v=5.199&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
        @Query("type") type: String = "post",
    ): LikesCountResponse

    @GET("likes.delete?v=5.199")
    suspend fun deleteLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
        @Query("type") type: String = "post",
    ): LikesCountResponse

    @GET("newsfeed.ignoreItem?v=5.199")
    suspend fun ignorePost(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") postId: Long,
        @Query("type") type: String = "wall",
    )

    @GET("wall.getComments?v=5.199")
    suspend fun fetchComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") postId: Long,
        @Query("extended") extended: Int = 1,
        @Query("fields") fields: String = "photo_100",
    ): CommentsResponse
}
