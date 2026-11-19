package ui.components

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

private const val YOUTUBE_BASE_URL = "https://www.barnaton.app"

@SuppressLint("SetJavaScriptEnabled")
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
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    configureYouTubeWebView()
                    loadYouTubeEmbed(safeKey)
                }
            },
            update = { view ->
                if (view.tag != safeKey) {
                    view.loadYouTubeEmbed(safeKey)
                }
            },
            onRelease = { view ->
                view.stopLoading()
                view.loadUrl("about:blank")
                view.destroy()
            },
            modifier = Modifier.matchParentSize()
        )
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureYouTubeWebView() {
    setBackgroundColor(android.graphics.Color.BLACK)
    settings.javaScriptEnabled = true
    settings.domStorageEnabled = true
    settings.mediaPlaybackRequiresUserGesture = false
    webChromeClient = WebChromeClient()
    webViewClient = WebViewClient()
}

private fun WebView.loadYouTubeEmbed(safeKey: String) {
    tag = safeKey
    loadDataWithBaseURL(YOUTUBE_BASE_URL, buildEmbedHtml(safeKey), "text/html", "utf-8", null)
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
