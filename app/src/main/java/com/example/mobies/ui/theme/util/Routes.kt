package com.example.mobies.ui.theme.util

import kotlinx.serialization.Serializable

@Serializable
data object HomeScreen

@Serializable
data object FavoritesScreen

@Serializable
data object SearchScreen

@Serializable
data class MovieDetailScreen(
    val id: Int,
    val title: String?,
    val poster: String?,
    val rating: String?,
    val desc: String?,
    val releaseDate: String?
)
