package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import data.Resource
import domain.model.TvVideo
import ui.theme.CardSurface
import ui.theme.Cerulean
import ui.theme.TextPrimary
import ui.theme.TextSecondary

@Composable
fun TrailerSection(
    stateVideos: Resource<List<TvVideo>>,
    onPlayClick: (TvVideo) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Trailers & Clips",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 20.dp, start = 16.dp, bottom = 8.dp)
        )

        when (stateVideos) {
            is Resource.Loading -> Box(
                modifier = Modifier.fillMaxWidth().height(90.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(strokeWidth = 2.dp)
            }

            is Resource.Error -> Text(
                text = "Failed to load trailers",
                color = TextSecondary,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)
            )

            is Resource.Success -> TrailerList(
                videos = stateVideos.data.orEmpty(),
                onPlayClick = onPlayClick
            )
        }
    }
}

@Composable
private fun TrailerList(
    videos: List<TvVideo>,
    onPlayClick: (TvVideo) -> Unit
) {
    if (videos.isEmpty()) {
        Text(
            text = "No trailers available.",
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, bottom = 24.dp)
        )
        return
    }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        items(videos) { video ->
            TrailerCard(video = video, onClick = { onPlayClick(video) })
        }
    }
}

@Composable
private fun TrailerCard(
    video: TvVideo,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(if (isFocused) 1.04f else 1f)

    Card(
        shape = RoundedCornerShape(8.dp),
        border = if (isFocused) BorderStroke(2.5.dp, Cerulean) else null,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        interactionSource = interactionSource,
        onClick = onClick,
        modifier = Modifier
            .width(196.dp)
            .scale(scale)
    ) {
        Column {
            TrailerThumbnail(key = video.key, title = video.name)
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = video.name,
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = video.type,
                    color = TextSecondary,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun TrailerThumbnail(key: String, title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
    ) {
        SubcomposeAsyncImage(
            model = "https://img.youtube.com/vi/$key/hqdefault.jpg",
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color(0x44000000)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xBB000000))
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White
                )
            }
        }
    }
}
