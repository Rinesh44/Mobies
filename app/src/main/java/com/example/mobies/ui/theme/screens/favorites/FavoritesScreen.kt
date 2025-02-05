package com.example.mobies.ui.theme.screens.favorites

import android.graphics.Movie
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobies.R
import com.example.mobies.data.entity.MovieEntity


@Composable
fun Favorites(
    favoritesViewModel: FavoritesViewmodel,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    onRemoveClick: (MovieEntity) -> Unit
) {
    val movieList = favoritesViewModel.favMovies.collectAsState().value
    if (movieList.isEmpty())
        NoFavorites()
    else FavoritesList(paddingValues, movieList, onRemoveClick)
}

@Composable
fun NoFavorites(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(stringResource(R.string.no_favorites_available))
    }
}


@Composable
fun FavoritesList(
    paddingValues: PaddingValues,
    movies: List<MovieEntity>,
    onRemoveClick: (MovieEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues), contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(movies) { movie ->
            FavoriteItem(movie, onRemoveClick)
        }
    }
}

@Composable
fun FavoriteItem(
    movie: MovieEntity,
    onRemoveClick: (MovieEntity) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w500" + movie.posterPath)
                .error(R.drawable.broken_image)
                .placeholder(R.drawable.image_placeholder)
                .crossfade(true)
                .build(),
            contentDescription = movie.title,
            modifier = Modifier
                .size(150.dp, 200.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    movie.title, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.close),
                    modifier = Modifier.clickable {
                        onRemoveClick(movie)
                    }
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(movie.description, overflow = TextOverflow.Ellipsis, fontSize = 15.sp)
        }
    }
}