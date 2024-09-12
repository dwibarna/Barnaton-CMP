package ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import barnaton_cmp.composeapp.generated.resources.Res
import barnaton_cmp.composeapp.generated.resources.background_tmdb
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    onSearch: ((String) -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(Res.drawable.background_tmdb),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.padding(start = 16.dp, top = 16.dp)
        )
        CustomSearchBar(
            onSearch = {
                onSearch?.invoke(it)
            }
        )
    }
}
