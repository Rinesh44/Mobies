package com.example.mobies.di

import com.example.mobies.data.dao.MovieDao
import com.example.mobies.data.repo.MoviesRepository
import com.example.mobies.data.repo.MoviesRepositoryImpl
import com.example.mobies.data.repo.OfflineMoviesRepo
import com.example.mobies.data.repo.OfflineMoviesRepoImpl
import com.example.mobies.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(apiService: ApiService): MoviesRepository {
        return MoviesRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideOfflineMovieRepo(movieDao: MovieDao): OfflineMoviesRepo{
        return OfflineMoviesRepoImpl(movieDao)
    }
}