package com.example.mobies.data.repo

import com.example.mobies.data.model.Result
import com.example.mobies.data.model.SearchResult
import com.example.mobies.network.ApiService

interface MoviesRepository {
    suspend fun getNowPlayingMovies(): List<Result>
    suspend fun getPopularMovies(): List<Result>
    suspend fun getUpcomingMovies(): List<Result>
    suspend fun getTopRatedMovies(): List<Result>
    suspend fun searchMovies(query: String): List<SearchResult>
}

class MoviesRepositoryImpl(private val apiService: ApiService) : MoviesRepository {
    override suspend fun getNowPlayingMovies(): List<Result> = apiService.getMovies().results
    override suspend fun getPopularMovies(): List<Result> = apiService.getPopularMovies().results
    override suspend fun getUpcomingMovies(): List<Result> = apiService.getUpcomingMovies().results
    override suspend fun getTopRatedMovies(): List<Result> = apiService.getTopRatedMovies().results
    override suspend fun searchMovies(query: String): List<SearchResult> = apiService.searchMovies(query).results
}
