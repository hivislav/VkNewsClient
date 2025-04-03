package com.hivislav.vknewsclient.presentation.news

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hivislav.vknewsclient.data.repository.NewsFeedRepository
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.utils.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel(
    context: Context,
) : ViewModel() {
    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exceptionHandler")
    }
    private val repository = NewsFeedRepository(context = context)

    private val newsFeedFlow = repository.newsFeedFlow

    private val loadNextDataEvents = MutableSharedFlow<Unit>()
    private val loadNextDataFlow = flow {
        loadNextDataEvents.collect {
            emit(
                NewsFeedScreenState.Posts(
                    posts = newsFeedFlow.value,
                    nextDataIsLoading = true
                )
            )
        }
    }

    val screenState = repository.newsFeedFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState}
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextNewsFeed() {
        viewModelScope.launch {
            loadNextDataEvents.emit(Unit)
            repository.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            repository.changeLikeStatus(feedPost = feedPost)
        }
    }

    fun deleteFeedPost(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            repository.deletePost(feedPost = feedPost)
        }
    }
}
