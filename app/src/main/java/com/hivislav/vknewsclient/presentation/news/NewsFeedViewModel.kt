package com.hivislav.vknewsclient.presentation.news

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.AuthInteractorImpl
import com.hivislav.vknewsclient.data.network.mapper.NewsFeedMapper
import com.hivislav.vknewsclient.data.network.retrofit.VkApiFactory
import com.hivislav.vknewsclient.domain.AuthInteractor
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    context: Context
) : ViewModel() {

    private val authInteractor: AuthInteractor = AuthInteractorImpl(context = context)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    init {
        loadNewsFeed()
    }

    private fun loadNewsFeed() {
        viewModelScope.launch {
            val token = authInteractor.getToken().firstOrNull()
            token?.let {
                val response = VkApiFactory.api.fetchNewsFeed(token = token)
                val domain = NewsFeedMapper().mapResponseToPosts(
                    responseDto = response
                )
                _screenState.value = NewsFeedScreenState.Posts(posts = domain)
            }
        }
    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        _screenState.value = NewsFeedScreenState.Posts(
            posts = currentState.posts.map {
                if (it == feedPost) {
                    val newStatistics = it.statistics.toMutableList().apply {
                        replaceAll { oldItem ->
                            if (oldItem.type == item.type) {
                                oldItem.copy(count = oldItem.count + 1)
                            } else oldItem
                        }
                    }
                    feedPost.copy(statistics = newStatistics)
                } else it
            }
        )
    }

    fun removeFeedPost(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return

        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)
    }
}
