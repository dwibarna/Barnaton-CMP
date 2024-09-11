package ui.screen.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import data.Resource
import domain.model.TvFavorite
import org.koin.compose.koinInject
import ui.components.RatingStar

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    id: Int = 0,
    viewModel: DetailViewModel = koinInject()
) {
    LaunchedEffect(id) {
        viewModel.getDetailData(id)
        viewModel.getTvFavorite(id)
    }

    val stateDetail by viewModel.uiStateDetail.collectAsState()
    val uiTvFavorite by viewModel.uiTvFavorite.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        when (stateDetail) {
            is Resource.Error -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = stateDetail.message ?: "error tidak di ketahui")
                }
            }

            is Resource.Loading -> {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is Resource.Success -> {
                Column(modifier = modifier) {
                    stateDetail.data?.let { data ->
                        with(data) {
                            val dataNow = TvFavorite(
                                id = id,
                                backdropPath = backdropPath,
                                name = name,
                                overview = overview
                            )

                            viewModel.getStateItemFavorite(/*viewModel.getTvFavorite(id).id == dataNow.id*/uiTvFavorite?.id == dataNow.id)
                            val stateFavorite by viewModel.stateItemFavorite.collectAsState()
                            SubcomposeAsyncImage(
                                model = posterPath,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                loading = {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }
                            )

                            Text(
                                text = name,
                                color = Color.White,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = modifier.padding(top = 16.dp, start = 16.dp)
                            )

                            Text(
                                text = episodeRunTime,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = modifier.padding(top = 16.dp, start = 16.dp)
                            )

                            Row(
                                modifier = modifier.padding(top = 16.dp, start = 16.dp)
                            ) {
                                RatingStar(
                                    rating = voteAverage / 2,
                                )
                                Text(
                                    text = voteAverage.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = modifier.padding(start = 8.dp)
                                )
                            }

                            Button(
                                modifier = modifier.padding(start = 16.dp, top = 8.dp),
                                onClick = {
                                    if (stateFavorite)
                                        viewModel.deleteTvFavorite(id = id)
                                    else
                                        viewModel.insertTvFavorite(dataNow)

                                    viewModel.getStateItemFavorite(stateFavorite.not())

                                }) {
                                Image(
                                    imageVector = if (stateFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = null
                                )

                                Text(
                                    text = if (stateFavorite) "Delete Favorite" else "Tambah Favorite",
                                    modifier = modifier.padding(start = 8.dp),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }

                            Text(
                                text = overview,
                                modifier = modifier.padding(top = 16.dp, start = 16.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}