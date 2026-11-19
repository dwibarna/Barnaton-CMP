package ui.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Resource
import org.koin.compose.koinInject
import ui.components.HomeSection
import ui.components.TvSeriesFavoriteList
import ui.theme.DarkBackground
import ui.theme.TextSecondary

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
            .background(DarkBackground)
    ) {
        HomeSection(title = "TV Series Favorite", modifier = Modifier.fillMaxSize()) {
            when (stateUi) {
                is Resource.Error -> Text(
                    text = stateUi.message ?: "Error tidak diketahui",
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                is Resource.Loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }

                is Resource.Success -> TvSeriesFavoriteList(
                    items = stateUi.data.orEmpty(),
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}
