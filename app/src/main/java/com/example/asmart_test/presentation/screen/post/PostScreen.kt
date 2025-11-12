package com.example.asmart_test.presentation.screen.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.asmart_test.core.UiState
import com.example.asmart_test.domain.model.post.PostResponse
import com.example.asmart_test.presentation.screen.post.component.PostItem
import com.example.asmart_test.presentation.screen.post.component.PostSkeleton
import com.example.asmart_test.presentation.viewmodel.PostViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostViewModel = koinViewModel(),
    goToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var isRefreshing by remember { mutableStateOf(false) }
    val stateRefresh = rememberPullToRefreshState()

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            IconButton(
                onClick = {
                    viewModel.logout { goToLogin() }
                }
            ) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.Logout, contentDescription = "")
            }
            Text(
                "Посты",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "")
            }
        }
        when (val state = uiState) {
            is UiState.Loading -> PostSkeleton()
            is UiState.Success<List<PostResponse>> -> {
                PullToRefreshBox(
                    state = stateRefresh,
                    isRefreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        viewModel.refreshPage {
                            isRefreshing = false
                        }
                    },
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = isRefreshing,
                            containerColor = MaterialTheme.colorScheme.surface,
                            color = MaterialTheme.colorScheme.primary,
                            state = stateRefresh
                        )
                    },
                ) {
                    LazyColumn(state = listState) {
                        items(state.data) { post ->
                            PostItem(post.title, post.body, post.imgUrl)
                        }

                        if (state.data.isNotEmpty()) {
                            item {
                                if (state.data.size.rem(10) == 0) {
                                    Text(
                                        "Загрузка...",
                                        modifier = Modifier.padding(16.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                LaunchedEffect(listState) {
                    snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                        .collect { lastVisible ->
                            val totalItems = state.data.size
                            if (lastVisible != null && lastVisible >= totalItems - 3) {
                                viewModel.loadNextPage()
                            }
                        }
                }
            }

            is UiState.Error -> Text("Ошибка: ${state.message}")
        }
    }
}