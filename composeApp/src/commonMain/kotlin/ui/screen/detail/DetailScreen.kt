package ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import data.Resource
import domain.model.ConnectionStatus
import domain.model.TvDetailSeries
import domain.model.TvFavorite
import domain.model.TvVideo
import domain.model.WatchRoomState
import org.koin.compose.koinInject
import ui.components.RatingStar
import ui.components.TrailerSection
import ui.components.VideoPlayer
import ui.components.YouTubePlayer
import ui.theme.CardSurface
import ui.theme.Cerulean
import ui.theme.DarkBackground
import ui.theme.LiveGreen
import ui.theme.StarGold
import ui.theme.StreamRed
import ui.theme.TextPrimary
import ui.theme.TextSecondary

private const val PREVIEW_HLS_STREAM = "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8"

private sealed interface ActiveMedia {
    data class HlsStream(val url: String) : ActiveMedia
    data class YouTubeTrailer(val video: TvVideo) : ActiveMedia
}

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    id: Int = 0,
    viewModel: DetailViewModel = koinInject(),
    onBackClick: (() -> Unit)? = null
) {
    LaunchedEffect(id) {
        viewModel.getDetailData(id)
        viewModel.getTvFavorite(id)
        viewModel.getTvVideos(id)
    }

    DisposableEffect(id) {
        viewModel.observeWatchRoom(id)
        onDispose { viewModel.leaveWatchRoom() }
    }

    val stateDetail by viewModel.uiStateDetail.collectAsState()
    val stateVideos by viewModel.uiStateVideos.collectAsState()
    val stateFavorite by viewModel.stateItemFavorite.collectAsState()
    val watchRoomState by viewModel.watchRoomState.collectAsState()

    Box(modifier = modifier.fillMaxSize().background(DarkBackground)) {
        when (stateDetail) {
            is Resource.Error -> CenterMessage(stateDetail.message ?: "Error tidak diketahui")
            is Resource.Loading -> CenterLoader()
            is Resource.Success -> stateDetail.data?.let { data ->
                DetailContent(
                    data = data,
                    stateVideos = stateVideos,
                    stateFavorite = stateFavorite,
                    watchRoomState = watchRoomState,
                    onBackClick = onBackClick,
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
        Text(text = message, color = TextSecondary)
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
    watchRoomState: WatchRoomState,
    onBackClick: (() -> Unit)?,
    onToggleFavorite: (TvFavorite) -> Unit
) {
    var activeMedia by remember { mutableStateOf<ActiveMedia?>(null) }
    val favoriteData = TvFavorite(data.id, data.backdropPath, data.name, data.overview)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        DetailHeaderMedia(
            posterPath = data.posterPath,
            title = data.name,
            activeMedia = activeMedia,
            onBackClick = onBackClick,
            onClosePlayer = { activeMedia = null }
        )
        DetailMetadata(data = data, watchRoomState = watchRoomState)
        DetailActionRow(
            stateFavorite = stateFavorite,
            onToggleFavorite = { onToggleFavorite(favoriteData) },
            onPlayPreview = { activeMedia = ActiveMedia.HlsStream(PREVIEW_HLS_STREAM) }
        )
        DetailOverview(overview = data.overview)
        TrailerSection(
            stateVideos = stateVideos,
            onPlayClick = { video ->
                activeMedia = if (video.site.equals("YouTube", ignoreCase = true) && video.key.isNotBlank()) {
                    ActiveMedia.YouTubeTrailer(video)
                } else {
                    ActiveMedia.HlsStream(PREVIEW_HLS_STREAM)
                }
            }
        )
    }
}

@Composable
private fun DetailHeaderMedia(
    posterPath: String,
    title: String,
    activeMedia: ActiveMedia?,
    onBackClick: (() -> Unit)?,
    onClosePlayer: () -> Unit
) {
    if (activeMedia != null) {
        ActiveMediaHeader(media = activeMedia, onClosePlayer = onClosePlayer)
        return
    }

    val scrim = Brush.verticalGradient(colors = listOf(Color.Transparent, DarkBackground))
    Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
        SubcomposeAsyncImage(
            model = posterPath,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            loading = { CenterLoader() }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(96.dp)
                .align(Alignment.BottomCenter)
                .background(scrim)
        )
        if (onBackClick != null) {
            OverlayIconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart).padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary
                )
            }
        }
    }
}

@Composable
private fun ActiveMediaHeader(
    media: ActiveMedia,
    onClosePlayer: () -> Unit
) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = Modifier.fillMaxWidth().background(Color.Black)) {
        when (media) {
            is ActiveMedia.HlsStream -> VideoPlayer(url = media.url)
            is ActiveMedia.YouTubeTrailer -> YouTubePlayer(videoKey = media.video.key)
        }
        Row(
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (media is ActiveMedia.YouTubeTrailer) {
                ExternalYouTubePill(
                    onClick = {
                        uriHandler.openUri("https://www.youtube.com/watch?v=${media.video.key}")
                    }
                )
            }
            OverlayIconButton(onClick = onClosePlayer) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close Player", tint = TextPrimary)
            }
        }
    }
}

@Composable
private fun ExternalYouTubePill(onClick: () -> Unit) {
    Text(
        text = "YouTube",
        color = TextPrimary,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xCC0B0F19))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    )
}

@Composable
private fun OverlayIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.background(Color(0x990B0F19), shape = RoundedCornerShape(50)),
        content = icon
    )
}

@Composable
private fun DetailMetadata(data: TvDetailSeries, watchRoomState: WatchRoomState) {
    Text(
        text = data.name,
        color = TextPrimary,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
    )
    Row(
        modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (data.episodeRunTime.isNotBlank()) {
            Text(
                text = data.episodeRunTime,
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        RatingStar(rating = data.voteAverage / 2)
        Text(
            text = ((data.voteAverage * 10).toInt() / 10.0).toString(),
            color = TextPrimary,
            style = MaterialTheme.typography.titleSmall
        )
    }
    WatchRoomBadge(
        state = watchRoomState,
        modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 16.dp)
    )
}

@Composable
private fun WatchRoomBadge(
    state: WatchRoomState,
    modifier: Modifier = Modifier
) {
    val (dotColor, label) = when (state.status) {
        ConnectionStatus.CONNECTED -> LiveGreen to "LIVE · ${state.viewerCount} watching"
        ConnectionStatus.CONNECTING -> StarGold to "Connecting live room..."
        ConnectionStatus.DISCONNECTED -> TextSecondary to "Live room offline"
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(CardSurface)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dotColor))
        Text(
            text = label,
            color = TextPrimary,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
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
            .padding(start = 16.dp, end = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FavoriteButton(isFavorite = stateFavorite, onClick = onToggleFavorite)
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = StreamRed),
            onClick = onPlayPreview
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = TextPrimary)
            Text(
                text = "Play HLS Stream",
                modifier = Modifier.padding(start = 4.dp),
                color = TextPrimary,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
private fun FavoriteButton(isFavorite: Boolean, onClick: () -> Unit) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFavorite) Cerulean else CardSurface
        ),
        onClick = onClick
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = if (isFavorite) Color.Red else TextPrimary
        )
        Text(
            text = if (isFavorite) "Delete Favorite" else "Tambah Favorite",
            modifier = Modifier.padding(start = 8.dp),
            color = TextPrimary,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
private fun DetailOverview(overview: String) {
    Text(
        text = "Overview",
        color = TextPrimary,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp)
    )
    Text(
        text = overview.ifBlank { "No overview available." },
        modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary,
        lineHeight = 20.sp
    )
}
