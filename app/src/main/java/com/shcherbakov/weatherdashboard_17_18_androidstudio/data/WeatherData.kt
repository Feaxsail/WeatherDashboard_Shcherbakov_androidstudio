package com.shcherbakov.weatherdashboard_17_18_androidstudio.data

data class WeatherData(
    val temperature: Int? = null,
    val humidity: Int? = null,
    val windSpeed: Int? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val loadingProgress: String = "",
    val weatherIndex: Int? = null
)