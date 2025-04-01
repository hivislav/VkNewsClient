package com.hivislav.vknewsclient.presentation.news

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hivislav.vknewsclient.domain.FeedPost

@Composable
fun NewsFeedScreen(
    paddingValues: PaddingValues,
    onCommentClick: (FeedPost) -> Unit
) {

    val viewModel: NewsFeedViewModel = viewModel(
        factory = NewsFeedViewModelFactory(context = LocalContext.current)
    )
    val screenState by viewModel.screenState.observeAsState(NewsFeedScreenState.Initial)

    when (val state = screenState) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                viewModel = viewModel,
                paddingValues = paddingValues,
                posts = state.posts,
                onCommentClick = onCommentClick
            )
        }

        NewsFeedScreenState.Initial -> {}
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun FeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    paddingValues: PaddingValues,
    onCommentClick: (FeedPost) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues),
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->
            val state = rememberDismissState()
            if (state.isDismissed(DismissDirection.EndToStart)) {
                viewModel.removeFeedPost(feedPost)
            }

            SwipeToDismiss(
                modifier = Modifier.animateItem(),
                state = state,
                background = {},
                directions = setOf(DismissDirection.EndToStart)
            ) {
                PostCard(
                    feedPost = feedPost,
                    onViewsClickListener = {
                        viewModel.updateCount(feedPost = feedPost, item = it)
                    },
                    onShareClickListener = {
                        viewModel.updateCount(feedPost = feedPost, item = it)
                    },
                    onCommentClickListener = {
                        onCommentClick(feedPost)
                    },
                    onLikeClickListener = {
                        viewModel.updateCount(feedPost = feedPost, item = it)
                    }
                )
            }
        }
    }
}
