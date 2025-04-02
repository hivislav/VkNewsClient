package com.hivislav.vknewsclient.presentation.comments

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hivislav.vknewsclient.domain.FeedPost

class CommentsViewModelFactory(
    private val feedPost: FeedPost,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost = feedPost, context = context) as T
    }
}
