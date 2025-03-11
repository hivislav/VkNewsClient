package com.hivislav.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val _feedPosts = MutableLiveData(emptyList<FeedPost>())
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts

    init {
        _feedPosts.value = List(20) {
            FeedPost(id = it.toLong())
        }
    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        _feedPosts.value = _feedPosts.value?.map {
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
    }

    fun removeFeedPost(feedPost: FeedPost) {
        val oldPosts = _feedPosts.value?.toMutableList() ?: mutableListOf()
        oldPosts.remove(feedPost)
        _feedPosts.value = oldPosts
    }
}
