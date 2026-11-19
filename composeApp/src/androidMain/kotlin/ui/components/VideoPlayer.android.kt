package ui.components

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.ui.PlayerView
import com.barna.barnaton.R

@OptIn(UnstableApi::class)
@Composable
actual fun VideoPlayer(
    url: String,
    modifier: Modifier,
    autoPlay: Boolean
) {
    val context = LocalContext.current
    val exoPlayer = remember(url) {
        ExoPlayer.Builder(context, buildRenderersFactory(context)).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            playWhenReady = autoPlay
        }
    }

    ExoPlayerLifecycleEffect(exoPlayer)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx -> inflatePlayerView(ctx, exoPlayer) },
            update = { view -> view.player = exoPlayer },
            modifier = Modifier.matchParentSize()
        )
    }
}

@Composable
private fun ExoPlayerLifecycleEffect(exoPlayer: ExoPlayer) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, exoPlayer) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) exoPlayer.pause()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }
}

private fun inflatePlayerView(ctx: Context, exoPlayer: ExoPlayer): PlayerView =
    (LayoutInflater.from(ctx).inflate(R.layout.view_video_player, null) as PlayerView).apply {
        player = exoPlayer
        keepScreenOn = true
        isFocusable = true
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

@OptIn(UnstableApi::class)
private fun buildRenderersFactory(context: Context): DefaultRenderersFactory {
    val factory = DefaultRenderersFactory(context).setEnableDecoderFallback(true)
    if (!isEmulator()) return factory

    // Bypass emulator goldfish hardware decoders that distort H264 YUV stride
    val selector = MediaCodecSelector { mimeType, requiresSecure, requiresTunneling ->
        val decoders = MediaCodecSelector.DEFAULT.getDecoderInfos(mimeType, requiresSecure, requiresTunneling)
        if (!mimeType.startsWith("video/")) return@MediaCodecSelector decoders
        val filtered = decoders
            .filterNot { it.name.contains("goldfish", ignoreCase = true) }
            .sortedByDescending { it.softwareOnly }
        filtered.ifEmpty { decoders }
    }
    return factory.setMediaCodecSelector(selector)
}

private fun isEmulator(): Boolean =
    Build.HARDWARE.contains("goldfish") ||
        Build.HARDWARE.contains("ranchu") ||
        Build.FINGERPRINT.startsWith("generic") ||
        Build.PRODUCT.contains("sdk") ||
        Build.MODEL.contains("Emulator")
