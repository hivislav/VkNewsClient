package com.hivislav.vknewsclient.data.repository

import android.content.Context
import com.hivislav.vknewsclient.data.AppDataStore
import com.hivislav.vknewsclient.data.network.mapper.NewsFeedMapper
import com.hivislav.vknewsclient.data.network.retrofit.VkApiFactory
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem
import com.hivislav.vknewsclient.domain.StatisticType
import kotlinx.coroutines.flow.firstOrNull

class NewsFeedRepository(context: Context) {
    private val appDataStore: AppDataStore = AppDataStore(context = context)

    private val api = VkApiFactory.api
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    suspend fun fetchNewsFeed(): List<FeedPost> {
        val response = api.fetchNewsFeed(token = getAccessToken())
        val posts = mapper.mapResponseToPosts(responseDto = response)
        _feedPosts.addAll(posts)
        return posts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
        val response = if (feedPost.isLiked) {
            api.deleteLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        } else {
            api.addLike(
                token = getAccessToken(),
                ownerId = feedPost.communityId,
                postId = feedPost.id
            )
        }
        val newLikesCount = response.likes.count ?: 0
        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }
        val newPost = feedPost.copy(
            statistics = newStatistics,
            isLiked = !feedPost.isLiked
        )
        val postIndex = _feedPosts.indexOf(feedPost)
        _feedPosts[postIndex] = newPost
    }

    private suspend fun getAccessToken(): String {
        return appDataStore.getToken().firstOrNull() ?: throw IllegalStateException("token is null")
    }
}
