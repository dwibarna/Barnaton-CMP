package ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import data.Resource
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import org.koin.compose.koinInject
import ui.components.RatingStar

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    id: Int = 0,
    viewModel: DetailViewModel = koinInject()
) {
    LaunchedEffect(id) {
        viewModel.getDetailData(id)
        viewModel.getTvFavorite(id)
    }

    val stateDetail by viewModel.uiStateDetail.collectAsState()
    val stateFavorite by viewModel.stateItemFavorite.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (stateDetail) {
            is Resource.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stateDetail.message ?: "Error tidak diketahui",
                        color = Color.White
                    )
                }
            }

            is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                stateDetail.data?.let { data ->
                    DetailContent(
                        data = data,
                        stateFavorite = stateFavorite,
                        onToggleFavorite = { favorite ->
                            if (stateFavorite) viewModel.deleteTvFavorite(id = id)
                            else viewModel.insertTvFavorite(favorite)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailContent(
    data: TvDetailSeries,
    stateFavorite: Boolean,
    onToggleFavorite: (TvFavorite) -> Unit
) {
    val favoriteData = TvFavorite(
        id = data.id,
        backdropPath = data.backdropPath,
        name = data.name,
        overview = data.overview
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            model = data.posterPath,
            contentDescription = data.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            loading = {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        )

        Text(
            text = data.name,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = data.episodeRunTime,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )

        Row(
            modifier = Modifier.padding(top = 12.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingStar(rating = data.voteAverage / 2)
            Text(
                text = data.voteAverage.toString(),
                color = Color.White,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        FavoriteButton(
            isFavorite = stateFavorite,
            onClick = { onToggleFavorite(favoriteData) },
            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
        )

        Text(
            text = data.overview,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 24.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) Color.Red else Color.White
        )
        Text(
            text = if (isFavorite) "Delete Favorite" else "Tambah Favorite",
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
