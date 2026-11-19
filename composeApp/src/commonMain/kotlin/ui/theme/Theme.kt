package ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BarnatonDarkColorScheme = darkColorScheme(
    primary = Cerulean,
    secondary = StarGold,
    background = DarkBackground,
    surface = DarkSurface,
    surfaceVariant = CardSurface,
    onPrimary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary
)

@Composable
fun BarnatonTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BarnatonDarkColorScheme,
        content = content
    )
}
