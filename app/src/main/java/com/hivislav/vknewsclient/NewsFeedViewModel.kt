package com.hivislav.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem
import com.hivislav.vknewsclient.ui.theme.NewsFeedScreenState

class NewsFeedViewModel : ViewModel() {
    private val initialSource = List(20) {
        FeedPost(id = it)
    }

    private val _screenState = MutableLiveData<NewsFeedScreenState>(NewsFeedScreenState.Posts(initialSource))
    val screenState: LiveData<NewsFeedScreenState> = _screenState

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
