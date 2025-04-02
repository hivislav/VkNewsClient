package com.hivislav.vknewsclient.data.repository

import android.content.Context
import com.hivislav.vknewsclient.data.AppDataStore
import com.hivislav.vknewsclient.data.network.mapper.NewsFeedMapper
import com.hivislav.vknewsclient.data.network.retrofit.VkApiFactory
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.PostComment
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

    private var nextFrom: String? = null

    suspend fun fetchNewsFeed(): List<FeedPost> {
        val startFrom = nextFrom
        if (startFrom == null && feedPosts.isNotEmpty()) return feedPosts

        val response = if (startFrom == null) {
            api.fetchNewsFeed(token = getAccessToken())
        } else api.fetchNewsFeed(token = getAccessToken(), startFrom = startFrom)

        nextFrom = response.newsFeedContent?.nextFrom
        val posts = mapper.mapResponseToPosts(newsFeedResponse = response)
        _feedPosts.addAll(posts)
        return feedPosts
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

    suspend fun deletePost(feedPost: FeedPost) {
        api.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        _feedPosts.remove(feedPost)
    }

    suspend fun fetchComments(feedPost: FeedPost): List<PostComment> {
        val response = api.fetchComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        return mapper.mapResponseToComments(commentsResponse = response)
    }

    private suspend fun getAccessToken(): String {
        return appDataStore.getToken().firstOrNull() ?: throw IllegalStateException("token is null")
    }
}
