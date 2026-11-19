package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun YouTubePlayer(
    videoKey: String,
    modifier: Modifier = Modifier
)
