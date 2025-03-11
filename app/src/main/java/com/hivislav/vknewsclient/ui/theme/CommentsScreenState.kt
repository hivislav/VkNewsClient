package com.hivislav.vknewsclient.ui.theme

import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.PostComment

sealed class CommentsScreenState {
    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) : CommentsScreenState()
    object Initial : CommentsScreenState()
}
