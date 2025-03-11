package com.hivislav.vknewsclient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hivislav.vknewsclient.domain.FeedPost

class CommentsViewModelFactory(private val feedPost: FeedPost) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost = feedPost) as T
    }
}
