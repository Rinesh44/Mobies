package com.example.mobies.ui.theme.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobies.data.entity.MovieEntity
import com.example.mobies.data.repo.OfflineMoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewmodel @Inject constructor(private val localRepo: OfflineMoviesRepo) : ViewModel() {

    val favMovies = localRepo.getAllMovies()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun removeFromFavorites(movie: MovieEntity) {
        viewModelScope.launch {
            localRepo.deleteMovie(movie)
        }
    }

}