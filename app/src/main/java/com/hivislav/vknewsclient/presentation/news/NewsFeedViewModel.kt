package com.hivislav.vknewsclient.presentation.news

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.repository.NewsFeedRepository
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    context: Context,
) : ViewModel() {

    private val repository = NewsFeedRepository(context = context)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    init {
        loadNewsFeed()
    }

    private fun loadNewsFeed() {
        viewModelScope.launch {
            val domain = repository.fetchNewsFeed()
            _screenState.value = NewsFeedScreenState.Posts(posts = domain)
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost = feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
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
