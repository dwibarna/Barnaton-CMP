package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    autoPlay: Boolean
) {
    val player = remember(url) {
        val nsUrl = NSURL.URLWithString(url)
        if (nsUrl != null) AVPlayer(uRL = nsUrl) else AVPlayer()
    }

    DisposableEffect(player, autoPlay) {
        if (autoPlay) player.play()
        onDispose {
            player.pause()
            player.replaceCurrentItemWithPlayerItem(null)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        UIKitViewController(
            factory = {
                AVPlayerViewController().apply {
                    this.player = player
                    this.showsPlaybackControls = true
                }
            },
            update = { viewController -> viewController.player = player },
            modifier = Modifier.matchParentSize()
        )
    }
}
