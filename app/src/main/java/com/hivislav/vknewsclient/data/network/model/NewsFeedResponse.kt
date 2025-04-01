package com.hivislav.vknewsclient.data.network.model

import com.google.gson.annotations.SerializedName

data class NewsFeedResponse(
    @SerializedName("response") val newsFeedContent: NewsFeedContentApi?,
)

data class NewsFeedContentApi(
    @SerializedName("items") val posts: List<PostApi>?,
    @SerializedName("groups") val groups: List<GroupApi>?
)

data class PostApi(
    @SerializedName("id") val id: Long,
    @SerializedName("source_id") val communityId: Long?,
    @SerializedName("text") val text: String?,
    @SerializedName("date") val date: Long?,
    @SerializedName("likes") val likes: LikesApi?,
    @SerializedName("comments") val comments: CommentsApi?,
    @SerializedName("views") val views: ViewsApi?,
    @SerializedName("reposts") val reposts: RepostsApi?,
    @SerializedName("attachments") val attachments: List<AttachmentApi>?
)

data class LikesApi(
    @SerializedName("count") val count: Int?,
    @SerializedName("user_likes") val userLikes: Int?
)

data class CommentsApi(
    @SerializedName("count") val count: Int?
)


data class ViewsApi(
    @SerializedName("count") val count: Int?
)

data class RepostsApi(
    @SerializedName("count") val count: Int?
)

data class AttachmentApi(
    @SerializedName("photo") val photo: PhotoApi?
)

data class PhotoApi(
    @SerializedName("sizes") val photoUrls: List<PhotoUrlApi>?
)

data class PhotoUrlApi(
    @SerializedName("url") val url: String?
)


data class GroupApi(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String?,
    @SerializedName("photo_200") val imageUrl: String?
)
