package com.hivislav.vknewsclient.presentation.news

import com.hivislav.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {
    data class Posts(
        val posts: List<FeedPost>,
        val nexDataIsLoading: Boolean = false,
    ) : NewsFeedScreenState()
    object Initial : NewsFeedScreenState()

    object Loading: NewsFeedScreenState()
}
