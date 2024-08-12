package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import domain.model.TvFavorite
import domain.model.TvSeries

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItem(
    tvSeries: TvSeries,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navigateToDetail?.invoke(tvSeries.id)
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            SubcomposeAsyncImage(
                model = tvSeries.backdropPath,
                contentDescription = tvSeries.name,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                modifier = modifier.fillMaxSize()
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = tvSeries.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = tvSeries.overview,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CardItemFavorite(
    tvFavorite: TvFavorite,
    modifier: Modifier = Modifier,
    navigateToDetail: ((Int) -> Unit)? = null
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navigateToDetail?.invoke(tvFavorite.id)
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            SubcomposeAsyncImage(
                model = tvFavorite.backdropPath,
                contentDescription = tvFavorite.name,
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                },
                modifier = modifier.fillMaxSize()
            )
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                Text(
                    text = tvFavorite.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    style = MaterialTheme.typography.caption
                )
                Text(
                    text = tvFavorite.overview,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
