package com.example.mobies.ui.theme.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.mobies.data.model.Result
import com.example.mobies.data.repo.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val homeRepository: MoviesRepository) :
    ViewModel() {

    sealed interface MovieState {
        data class Success(val result: List<Result>) : MovieState
        object Error : MovieState
        object Loading : MovieState
    }

    var nowPlayingState by mutableStateOf<MovieState>(MovieState.Loading)
        private set
    var popularMoviesState by mutableStateOf<MovieState>(MovieState.Loading)
        private set
    var upcomingMoviesState by mutableStateOf<MovieState>(MovieState.Loading)
        private set
    var topRatedMoviesState by mutableStateOf<MovieState>(MovieState.Loading)
        private set

    init {
        fetchMovies(homeRepository::getNowPlayingMovies) { nowPlayingState = it }
        fetchMovies(homeRepository::getPopularMovies) { popularMoviesState = it }
        fetchMovies(homeRepository::getUpcomingMovies) { upcomingMoviesState = it }
        fetchMovies(homeRepository::getTopRatedMovies) { topRatedMoviesState = it }
    }

    private fun fetchMovies(
        fetch: suspend () -> List<Result>,
        stateSetter: (MovieState) -> Unit
    ) {
        viewModelScope.launch {
            stateSetter(MovieState.Loading)
            stateSetter(
                try {
                    MovieState.Success(fetch())
                } catch (e: IOException) {
                    MovieState.Error
                } catch (e: HttpException) {
                    MovieState.Error
                }
            )
        }
    }
}