package com.hivislav.vknewsclient.data.network.mapper

import com.hivislav.vknewsclient.data.network.model.CommentsResponse
import com.hivislav.vknewsclient.data.network.model.NewsFeedResponse
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.domain.PostComment
import com.hivislav.vknewsclient.domain.StatisticItem
import com.hivislav.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {
    fun mapResponseToPosts(newsFeedResponse: NewsFeedResponse): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = newsFeedResponse.newsFeedContent?.posts ?: emptyList()
        val groups = newsFeedResponse.newsFeedContent?.groups ?: emptyList()

        for (post in posts) {
            val group = groups.find { it.id == post.communityId?.absoluteValue } ?: continue
            val feedPost = FeedPost(
                id = post.id,
                communityName = group.name ?: "",
                communityId = post.communityId ?: 0,
                publicationDate = post.date?.mapTimeStampToDate() ?: "",
                communityImageUrl = group.imageUrl ?: "",
                contentText = post.text ?: "",
                contentImageUrl = post.attachments?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, post.likes?.count ?: 0),
                    StatisticItem(type = StatisticType.VIEWS, post.views?.count ?: 0),
                    StatisticItem(type = StatisticType.SHARES, post.reposts?.count ?: 0),
                    StatisticItem(type = StatisticType.COMMENTS, post.comments?.count ?: 0)
                ),
                isLiked = (post.likes?.userLikes ?: 0) > 0
            )
            result.add(feedPost)
        }
        return result
    }

    fun mapResponseToComments(commentsResponse: CommentsResponse): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = commentsResponse.content?.comments ?: emptyList()
        val profiles = commentsResponse.content?.profiles ?: emptyList()


        for (comment in comments) {
            val author = profiles.firstOrNull { it.id == comment.authorId } ?: continue
            val postComment = PostComment(
                id = comment.id,
                authorName = "${author.firstName} ${author.lastName}",
                authorAvatarUrl = author.avatarUrl ?: "",
                commentText = comment.text ?: "",
                publicationDate = comment.date?.mapTimeStampToDate() ?: ""
            )
            result.add(postComment)
        }
        return result
    }

    private fun Long.mapTimeStampToDate(): String {
        val date = Date(this * 1000L)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}
