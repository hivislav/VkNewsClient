package com.hivislav.vknewsclient.presentation.comments

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hivislav.vknewsclient.data.repository.NewsFeedRepository
import com.hivislav.vknewsclient.domain.FeedPost
import kotlinx.coroutines.flow.map

class CommentsViewModel(
    feedPost: FeedPost,
    context: Context,
) : ViewModel() {
    private val repository = NewsFeedRepository(context = context)

    val screenState = repository.fetchComments(feedPost = feedPost)
        .map {
            CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = it
            )
        }
}
