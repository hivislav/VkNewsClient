package com.hivislav.vknewsclient.data.network.model

import com.google.gson.annotations.SerializedName

data class LikesCountResponse(
    @SerializedName("response") val likes: LikesCountApi,
)

data class LikesCountApi(
    @SerializedName("likes") val count: Int?,
)
