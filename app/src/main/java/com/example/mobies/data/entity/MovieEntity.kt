package com.example.mobies.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorites")
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val posterPath: String,
    val description: String,
    val releaseDate: String,
    val rating: String,
    val isFavorite: Boolean
)
