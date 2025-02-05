package com.example.mobies.utils

import com.example.mobies.data.entity.MovieEntity
import com.example.mobies.data.model.Result

fun Result.toMovieEntity(): MovieEntity = MovieEntity(
    id = id ?: 0,
    title = title ?: "",
    posterPath = posterPath ?: "",
    description = overview ?: "",
    rating = voteAverage.toString(),
    releaseDate = releaseDate.toString(),
    isFavorite = false
)

fun MovieEntity.toMovieModel(): Result = Result(
    id = id,
    title = title,
    posterPath = posterPath,
    overview = description,
    voteAverage = rating.toDouble(),
)