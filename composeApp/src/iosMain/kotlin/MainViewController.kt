import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin

//fun MainViewController() = ComposeUIViewController { App() }
fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}