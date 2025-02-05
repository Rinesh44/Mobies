package com.example.mobies.data.repo

import com.example.mobies.data.dao.MovieDao
import com.example.mobies.data.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

interface OfflineMoviesRepo {
    suspend fun insertMovie(movie: MovieEntity)
    suspend fun deleteMovie(movie: MovieEntity)
    fun getAllMovies(): Flow<List<MovieEntity>>
    suspend fun isFavorite(id: Int): Flow<Boolean>
}

class OfflineMoviesRepoImpl(private val movieDao: MovieDao) : OfflineMoviesRepo {
    override suspend fun insertMovie(movie: MovieEntity) = movieDao.insertMovie(movie)
    override suspend fun deleteMovie(movie: MovieEntity) = movieDao.deleteMovie(movie)
    override fun getAllMovies(): Flow<List<MovieEntity>> = movieDao.getAllMovies()
    override suspend fun isFavorite(id: Int): Flow<Boolean>  = movieDao.isFavorite(id)
}