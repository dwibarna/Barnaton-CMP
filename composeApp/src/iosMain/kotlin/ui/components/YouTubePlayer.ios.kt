package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.WebKit.WKAudiovisualMediaTypeNone
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

private const val YOUTUBE_BASE_URL = "https://www.barnaton.app"

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun YouTubePlayer(
    videoKey: String,
    modifier: Modifier
) {
    val safeKey = sanitizeYouTubeKey(videoKey)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        key(safeKey) {
            UIKitView(
                factory = { createConfiguredWebView(safeKey) },
                onRelease = { webView ->
                    webView.stopLoading()
                    webView.loadHTMLString("", baseURL = null)
                },
                modifier = Modifier.matchParentSize()
            )
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun createConfiguredWebView(safeKey: String): WKWebView {
    val config = WKWebViewConfiguration().apply {
        allowsInlineMediaPlayback = true
        mediaTypesRequiringUserActionForPlayback = WKAudiovisualMediaTypeNone
    }
    return WKWebView(frame = CGRectZero.readValue(), configuration = config).apply {
        loadHTMLString(
            string = buildEmbedHtml(safeKey),
            baseURL = NSURL.URLWithString(YOUTUBE_BASE_URL)
        )
    }
}

private fun sanitizeYouTubeKey(videoKey: String): String =
    videoKey.filter { it.isLetterOrDigit() || it == '_' || it == '-' }

private fun buildEmbedHtml(safeKey: String): String = """
    <!DOCTYPE html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
        <style>
            html, body { margin: 0; padding: 0; width: 100%; height: 100%; background: #000; overflow: hidden; }
            iframe { width: 100%; height: 100%; border: 0; }
        </style>
    </head>
    <body>
        <iframe src="https://www.youtube.com/embed/$safeKey?autoplay=1&playsinline=1&rel=0"
            referrerpolicy="strict-origin-when-cross-origin"
            allow="autoplay; encrypted-media; picture-in-picture"
            allowfullscreen>
        </iframe>
    </body>
    </html>
""".trimIndent()
