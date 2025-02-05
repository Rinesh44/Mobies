package com.example.mobies.ui.theme.screens.movie_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobies.R
import com.example.mobies.data.entity.MovieEntity

@Composable
fun MovieDetailScreen(
    movieId: Int,
    movieTitle: String,
    moviePoster: String,
    movieRating: String,
    movieDesc: String,
    movieReleaseDate: String,
    movieDetailViewModel: MovieDetailViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        Card(
            shape = ShapeDefaults.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://image.tmdb.org/t/p/w500" + moviePoster)
                    .placeholder(R.drawable.image_placeholder)
                    .error(R.drawable.broken_image)
                    .crossfade(true)
                    .build(),
                contentDescription = movieTitle,
                modifier = modifier.size(200.dp, 300.dp)
            )
        }

        val formatterRating = String.format("%.1f", movieRating.toFloat())
        Spacer(modifier.size(16.dp))
        Text(
            text = movieTitle,
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Rating: $formatterRating",
            modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Release Date: ${movieReleaseDate}",
            modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier.size(16.dp))
        Text(text = "Synopsis", modifier.padding(horizontal = 16.dp))
        Spacer(modifier.size(4.dp))
        Text(text = movieDesc, modifier.padding(horizontal = 16.dp))

        Box(modifier = Modifier.fillMaxSize()) {
            if(!movieDetailViewModel.isFavorite(movieId))
            FloatingActionButton(
                onClick = {
                        movieDetailViewModel.addToFavorites(
                            MovieEntity(
                                id = 0,
                                title = movieTitle,
                                posterPath = moviePoster,
                                description = movieDesc,
                                rating = movieRating,
                                releaseDate = movieReleaseDate,
                                isFavorite = true
                            )
                        )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = stringResource(R.string.favorite_screen)
                )
            }
        }
    }
}
