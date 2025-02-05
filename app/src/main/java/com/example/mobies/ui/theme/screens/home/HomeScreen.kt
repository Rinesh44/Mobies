package com.example.mobies.ui.theme.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.example.mobies.R
import com.example.mobies.data.model.Result

@Composable
fun Home(
    nowPlayingState: HomeViewmodel.MovieState,
    upcomingState: HomeViewmodel.MovieState,
    topRatedMoviesState: HomeViewmodel.MovieState,
    popularState: HomeViewmodel.MovieState,
    retryAction: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onItemClick: (Result?) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        MovieSection(nowPlayingState, retryAction) {
            UIHolder(
                "Now Playing",
                it,
                onclick = onItemClick
            )
        }
        MovieSection(upcomingState, retryAction) { UIHolder("Upcoming", it, onclick = onItemClick) }
        MovieSection(topRatedMoviesState, retryAction) {
            UIHolder(
                "Top Rated",
                it,
                onclick = onItemClick
            )
        }
        MovieSection(popularState, retryAction) { UIHolder("Popular", it, onclick = onItemClick) }
    }
}

@Composable
fun MovieSection(
    state: HomeViewmodel.MovieState,
    retryAction: () -> Unit,
    content: @Composable (List<Result>) -> Unit
) {
    when (state) {
        is HomeViewmodel.MovieState.Error -> ErrorUI(retryAction)
        is HomeViewmodel.MovieState.Loading -> LoadingUI()
        is HomeViewmodel.MovieState.Success -> content(state.result)
    }
}


@Composable
fun ErrorUI(
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.error_loading),
        )
        Button(
            onClick = { retryAction.invoke() }
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

@Composable
fun LoadingUI(
    modifier: Modifier = Modifier
) {
    Text(text = stringResource(R.string.loading))
}

@Composable
fun UIHolder(
    title: String,
    result: List<Result>,
    modifier: Modifier = Modifier,
    onclick: (Result?) -> Unit
) {
    Spacer(modifier = modifier.padding(10.dp))
    Text(
        text = title, modifier.padding(horizontal = 16.dp),
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.padding(4.dp))
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(result) { item ->
            MovieCard(item, onclick = onclick)
        }
    }
}


@Composable
fun MovieCard(result: Result?, width: Int = 150, height: Int = 210, onclick: (Result?) -> Unit) {
    Box(
        modifier = Modifier
            .size(width.dp, height.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Gray)
            .clickable {
                onclick(result)
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w500" + result?.posterPath)
                .crossfade(true)
                .scale(Scale.FIT)
                .build(),
            contentDescription = stringResource(R.string.image_poster),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
