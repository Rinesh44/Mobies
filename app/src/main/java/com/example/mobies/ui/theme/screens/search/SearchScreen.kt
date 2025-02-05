package com.example.mobies.ui.theme.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mobies.R
import com.example.mobies.data.model.SearchResult

@Composable
fun Search(
    searchState: SearchViewmodel.SearchState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    searchEvent: (String) -> Unit,
    onclick: (SearchResult) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(contentPadding)
    )
    {
        SearchBox {
            searchEvent(it)
        }


        when (searchState) {
            SearchViewmodel.SearchState.Error -> {

            }

            SearchViewmodel.SearchState.Loading -> {

            }

            is SearchViewmodel.SearchState.Success -> {
                val result = searchState.searchResult
                if (result.isEmpty()) {
                    Text(text = "No results found")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(result) { item ->
                            SearchedItem(
                                movieSearchItem = MovieSearchItem(
                                    item.title ?: "",
                                    item.posterPath ?: ""
                                )
                            ) {
                                onclick(item)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBox(
    searchEvent: (String) -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TextField(
        value = query,
        onValueChange = {
            query = it
            if (query.isNotBlank()) {
                searchEvent(query)
            }
        },
        placeholder = { Text(stringResource(R.string.search_movies)) },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            unfocusedLabelColor = Color.Black,
            focusedContainerColor = Color.White,
            focusedLabelColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.search_movies)
            )
        },
        singleLine = true,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

@Composable
fun SearchedItem(
    modifier: Modifier = Modifier,
    movieSearchItem: MovieSearchItem,
    onClick: (MovieSearchItem) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().clickable {
            onClick(movieSearchItem)
        }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://image.tmdb.org/t/p/w500" + movieSearchItem.imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.broken_image)
                .build(),
            contentDescription = stringResource(R.string.movie_poster),
            modifier.size(70.dp, 120.dp)
        )

        Spacer(modifier.size(6.dp))
        Text(
            text = movieSearchItem.title,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

data class MovieSearchItem(
    val title: String,
    val imageUrl: String,
)