package com.hivislav.vknewsclient.data.repository

import android.content.Context
import com.hivislav.vknewsclient.data.AppDataStore
import com.hivislav.vknewsclient.data.network.mapper.NewsFeedMapper
import com.hivislav.vknewsclient.data.network.retrofit.VkApiFactory
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.PostComment
import com.hivislav.vknewsclient.domain.StatisticItem
import com.hivislav.vknewsclient.domain.StatisticType
import com.hivislav.vknewsclient.utils.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class NewsFeedRepository(context: Context) {
    private val appDataStore: AppDataStore = AppDataStore(context = context)

    private val api = VkApiFactory.api
    private val mapper = NewsFeedMapper()

    private val scope = CoroutineScope(Dispatchers.IO)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private var nextFrom: String? = null

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom
            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                api.fetchNewsFeed(token = getAccessToken())
            } else api.fetchNewsFeed(token = getAccessToken(), startFrom = startFrom)

            nextFrom = response.newsFeedContent?.nextFrom
            val posts = mapper.mapResponseToPosts(newsFeedResponse = response)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    val newsFeedFlow: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = scope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
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
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        api.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            postId = feedPost.id
        )

        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
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
