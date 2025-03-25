package com.hivislav.vknewsclient.presentation.comments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hivislav.vknewsclient.domain.PostComment
import com.hivislav.vknewsclient.domain.FeedPost
import com.hivislav.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun CommentsScreen(
    feedPost: FeedPost,
    onBackPressed: () -> Unit,
) {

    val viewModel: CommentsViewModel = viewModel(
        factory = CommentsViewModelFactory(feedPost)
    )
    val screenState by viewModel.screenState.observeAsState(CommentsScreenState.Initial)

    val currentState = screenState

    if (currentState is CommentsScreenState.Comments) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Comments for FeedPost Id: ${currentState.feedPost.id}"
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackPressed
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = ""
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 72.dp
                )
            ) {
                items(
                    items = currentState.comments,
                    key = { it.id }
                ) { comment ->
                    CommentItem(
                        comment = comment
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: PostComment,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(id = comment.authorAvatarId),
            contentDescription = ""
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = "${comment.authorName} CommentId: ${comment.id}",
                color = MaterialTheme.colors.onPrimary,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.commentText,
                color = MaterialTheme.colors.onPrimary,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = comment.publicationDate,
                color = MaterialTheme.colors.onSecondary,
                fontSize = 12.sp
            )
        }
    }
}

@Preview
@Composable
private fun PreviewComment() {
    VkNewsClientTheme {
        CommentItem(comment = PostComment(1))
    }
}
