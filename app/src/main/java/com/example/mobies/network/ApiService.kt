package com.example.mobies.network

import com.example.mobies.data.model.NowPlayingMoviesModel
import com.example.mobies.data.model.SearchedMovieModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("movie/now_playing")
    suspend fun getMovies(): NowPlayingMoviesModel

    @GET("movie/popular")
    suspend fun getPopularMovies(): NowPlayingMoviesModel

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): NowPlayingMoviesModel

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): NowPlayingMoviesModel

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String
    ): SearchedMovieModel
}