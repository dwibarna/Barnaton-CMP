package ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ui.theme.StarGold

private val EmptyStarColor = Color(0xFF475569)

@Composable
fun RatingStar(
    rating: Float = 5f,
    maxRating: Int = 5,
) {
    Row {
        for (i in 1..maxRating) {
            when {
                i <= rating.toInt() -> StarIcon(tint = StarGold)
                i == rating.toInt() + 1 && rating % 1 != 0f -> PartialStar(fraction = rating % 1)
                else -> StarIcon(tint = EmptyStarColor)
            }
        }
    }
}

@Composable
private fun StarIcon(tint: Color) {
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(20.dp)
    )
}

@Composable
private fun PartialStar(fraction: Float) {
    val customShape = FractionalClipShape(fraction)
    Box {
        StarIcon(tint = EmptyStarColor)
        Box(modifier = Modifier.graphicsLayer(clip = true, shape = customShape)) {
            StarIcon(tint = StarGold)
        }
    }
}

private class FractionalClipShape(private val fraction: Float) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            rect = Rect(left = 0f, top = 0f, right = size.width * fraction, bottom = size.height)
        )
    }
}
