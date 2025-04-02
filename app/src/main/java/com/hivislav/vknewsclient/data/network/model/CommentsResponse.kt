package com.hivislav.vknewsclient.data.network.model

import com.google.gson.annotations.SerializedName

data class CommentsResponse(
    @SerializedName("response") val content: CommentsContentApi?,
)

data class CommentsContentApi(
    @SerializedName("items") val comments: List<CommentApi>?,
    @SerializedName("profiles") val profiles: List<ProfileApi>?,
)

data class CommentApi(
    @SerializedName("id") val id: Long,
    @SerializedName("date") val date: Long?,
    @SerializedName("from_id") val authorId: Long?,
    @SerializedName("text") val text: String?
)

data class ProfileApi(
    @SerializedName("id") val id: Long,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("photo_100") val avatarUrl: String?
)
