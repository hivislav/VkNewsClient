package com.hivislav.vknewsclient.presentation.news

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.repository.NewsFeedRepository
import com.hivislav.vknewsclient.domain.FeedPost
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    context: Context,
) : ViewModel() {

    private val repository = NewsFeedRepository(context = context)

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Initial)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadNewsFeed()
    }

    private fun loadNewsFeed() {
        viewModelScope.launch {
            val domain = repository.fetchNewsFeed()
            _screenState.value = NewsFeedScreenState.Posts(posts = domain)
        }
    }

    fun loadNextNewsFeed() {
        _screenState.value = NewsFeedScreenState.Posts(
            posts = repository.feedPosts,
            nexDataIsLoading = true
        )
        loadNewsFeed()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost = feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
        }
    }

    fun deleteFeedPost(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.deletePost(feedPost = feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
        }
    }
}
