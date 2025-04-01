package com.hivislav.vknewsclient.data.network.mapper

import com.hivislav.vknewsclient.data.network.model.NewsFeedResponse
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.StatisticItem
import com.hivislav.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {
    fun mapResponseToPosts(responseDto: NewsFeedResponse): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent?.posts ?: emptyList()
        val groups = responseDto.newsFeedContent?.groups ?: emptyList()

        for (post in posts) {
            val group = groups.find { it.id == post.communityId?.absoluteValue } ?: continue
            val feedPost = FeedPost(
                id = post.id,
                communityName = group.name ?: "",
                publicationDate = ((post.date ?: 0) * 1000L).mapTimeStampToDate(),
                communityImageUrl = group.imageUrl ?: "",
                contentText = post.text ?: "",
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes?.count ?: 0),
                    StatisticItem(type = StatisticType.VIEWS, post.views?.count ?: 0),
                    StatisticItem(type = StatisticType.SHARES, post.reposts?.count ?: 0),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments?.count ?: 0)
                ),
                isFavorite = post.isFavorite ?: false
            )
            result.add(feedPost)
        }
        return result
    }

    private fun Long.mapTimeStampToDate(): String {
        val date = Date(this)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}
