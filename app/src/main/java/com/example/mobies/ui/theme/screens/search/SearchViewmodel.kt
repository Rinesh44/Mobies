package com.example.mobies.ui.theme.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.mobies.data.model.SearchResult
import com.example.mobies.data.repo.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchViewmodel @Inject constructor(private val repo: MoviesRepository) : ViewModel() {

    sealed interface SearchState {
        data class Success(val searchResult: List<SearchResult>) : SearchState
        object Error : SearchState
        object Loading : SearchState
    }

    var searchState by mutableStateOf<SearchState>(SearchState.Loading)
        private set

    fun searchMovies(query: String) {
        viewModelScope.launch {
            searchState = try {
                val searchResult = repo.searchMovies(query)
                SearchState.Success(searchResult)
            } catch (e: IOException) {
                SearchState.Error
            } catch (e: HttpException) {
                SearchState.Error
            }
        }
    }
}