package ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import data.Resource
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvVideo
import org.koin.compose.koinInject
import ui.components.RatingStar
import ui.components.TrailerSection
import ui.components.VideoPlayer

private const val PREVIEW_HLS_STREAM = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    id: Int = 0,
    viewModel: DetailViewModel = koinInject()
) {
    LaunchedEffect(id) {
        viewModel.getDetailData(id)
        viewModel.getTvFavorite(id)
        viewModel.getTvVideos(id)
    }

    val stateDetail by viewModel.uiStateDetail.collectAsState()
    val stateVideos by viewModel.uiStateVideos.collectAsState()
    val stateFavorite by viewModel.stateItemFavorite.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (stateDetail) {
            is Resource.Error -> CenterMessage(stateDetail.message ?: "Error tidak diketahui")
            is Resource.Loading -> CenterLoader()
            is Resource.Success -> stateDetail.data?.let { data ->
                DetailContent(
                    data = data,
                    stateVideos = stateVideos,
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

@Composable
private fun CenterMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.White)
    }
}

@Composable
private fun CenterLoader() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun DetailContent(
    data: TvDetailSeries,
    stateVideos: Resource<List<TvVideo>>,
    stateFavorite: Boolean,
    onToggleFavorite: (TvFavorite) -> Unit
) {
    var activeStreamUrl by remember { mutableStateOf<String?>(null) }
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
        DetailHeaderMedia(
            posterPath = data.posterPath,
            title = data.name,
            activeStreamUrl = activeStreamUrl,
            onClosePlayer = { activeStreamUrl = null }
        )

        DetailMetadata(data = data)

        DetailActionRow(
            stateFavorite = stateFavorite,
            onToggleFavorite = { onToggleFavorite(favoriteData) },
            onPlayPreview = { activeStreamUrl = PREVIEW_HLS_STREAM }
        )

        Text(
            text = data.overview,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )

        TrailerSection(
            stateVideos = stateVideos,
            onPlayClick = { activeStreamUrl = PREVIEW_HLS_STREAM }
        )
    }
}

@Composable
private fun DetailHeaderMedia(
    posterPath: String,
    title: String,
    activeStreamUrl: String?,
    onClosePlayer: () -> Unit
) {
    if (activeStreamUrl != null) {
        Box(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
            VideoPlayer(url = activeStreamUrl)
            IconButton(
                onClick = onClosePlayer,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color(0x88000000), shape = RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Player",
                    tint = Color.White
                )
            }
        }
        return
    }

    SubcomposeAsyncImage(
        model = posterPath,
        contentDescription = title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().height(300.dp),
        loading = { CenterLoader() }
    )
}

@Composable
private fun DetailMetadata(data: TvDetailSeries) {
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
}

@Composable
private fun DetailActionRow(
    stateFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onPlayPreview: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FavoriteButton(isFavorite = stateFavorite, onClick = onToggleFavorite)
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE50914)),
            onClick = onPlayPreview
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                tint = Color.White
            )
            Text(
                text = "Play Preview",
                modifier = Modifier.padding(start = 4.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    Button(onClick = onClick) {
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
