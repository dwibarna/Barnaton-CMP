package ui.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import data.Resource
import org.koin.compose.koinInject
import ui.components.HomeSection
import ui.components.TvSeriesFavoriteList

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = koinInject(),
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    LaunchedEffect(Unit) {
        viewModel.getAllTvFavorite()
    }
    val stateUi by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HomeSection(title = "TV Series Favorite") {
            when (stateUi) {
                is Resource.Error -> {
                    Text(text = stateUi.message ?: "error tidak di ketahui")
                }

                is Resource.Loading -> {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    TvSeriesFavoriteList(
                        items = stateUi.data ?: emptyList(),
                        navigateToDetail = navigateToDetail
                    )
                }
            }
        }
    }
}