package ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import domain.model.TvFavorite
import domain.model.TvSeries
import ui.theme.CardSurface
import ui.theme.Cerulean
import ui.theme.StarGold
import ui.theme.TextPrimary
import ui.theme.TextSecondary

@Composable
fun CardItem(
    tvSeries: TvSeries,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    val formattedRating = ((tvSeries.voteAverage * 10).toInt() / 10.0).toString()
    MediaBackdropCard(
        imageUrl = tvSeries.backdropPath,
        title = tvSeries.name,
        overview = tvSeries.overview,
        ratingBadge = "★ $formattedRating",
        modifier = modifier,
        onClick = { navigateToDetail?.invoke(tvSeries.id) }
    )
}

@Composable
fun CardItemFavorite(
    tvFavorite: TvFavorite,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    MediaBackdropCard(
        imageUrl = tvFavorite.backdropPath,
        title = tvFavorite.name,
        overview = tvFavorite.overview,
        ratingBadge = null,
        modifier = modifier,
        onClick = { navigateToDetail?.invoke(tvFavorite.id) }
    )
}

@Composable
private fun MediaBackdropCard(
    imageUrl: String,
    title: String,
    overview: String,
    ratingBadge: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val scale by animateFloatAsState(if (isFocused) 1.02f else 1f)
    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color(0x88000000), Color(0xE60B0F19))
    )
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(vertical = 6.dp, horizontal = 16.dp)
            .scale(scale),
        shape = RoundedCornerShape(12.dp),
        border = if (isFocused) BorderStroke(2.5.dp, Cerulean) else null,
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        interactionSource = interactionSource,
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(gradient))
            if (ratingBadge != null) {
                RatingPill(
                    text = ratingBadge,
                    modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)
                )
            }
            CardCaption(title = title, overview = overview)
        }
    }
}

@Composable
private fun RatingPill(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = StarGold,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(Color(0xB30B0F19), shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}

@Composable
private fun CardCaption(title: String, overview: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(14.dp),
        verticalArrangement = Arrangement.Bottom,
    ) {
        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = overview,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = TextSecondary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
