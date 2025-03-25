package com.hivislav.vknewsclient.presentation.news

import com.hivislav.vknewsclient.domain.FeedPost

sealed class NewsFeedScreenState {
    data class Posts(val posts: List<FeedPost>) : NewsFeedScreenState()
    object Initial: NewsFeedScreenState()
}
