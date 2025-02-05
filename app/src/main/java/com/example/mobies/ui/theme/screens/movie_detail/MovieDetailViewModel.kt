package com.example.mobies.ui.theme.screens.movie_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobies.data.entity.MovieEntity
import com.example.mobies.data.repo.OfflineMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(private val offlineMovieRepo: OfflineMoviesRepo) :
    ViewModel() {

    fun addToFavorites(movie: MovieEntity) {
        viewModelScope.launch {
            offlineMovieRepo.insertMovie(movie)
        }
    }

    fun isFavorite(movieId: Int): Boolean {
        var isFav = false
        viewModelScope.launch {
            offlineMovieRepo.isFavorite(movieId).collect {
                isFav = it
            }
        }

        return isFav
    }

}