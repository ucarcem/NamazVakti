package com.example.namazvakti.ui.location

data class LocationUiState(
    val searchQuery: String = "",
    val cities: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)