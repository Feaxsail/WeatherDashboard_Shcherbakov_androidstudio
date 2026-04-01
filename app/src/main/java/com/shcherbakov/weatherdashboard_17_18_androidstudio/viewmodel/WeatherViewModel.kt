package com.shcherbakov.weatherdashboard_17_18_androidstudio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shcherbakov.weatherdashboard_17_18_androidstudio.data.WeatherData
import com.shcherbakov.weatherdashboard_17_18_androidstudio.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository()

    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()

    init {
        loadWeatherData()
        startAutoRefresh()
    }

    fun loadWeatherData() {
        viewModelScope.launch {
            _weatherState.value = WeatherData(isLoading = true, error = null, loadingProgress = "Начало загрузки...")
            try {
                coroutineScope {
                    _weatherState.value = _weatherState.value.copy(loadingProgress = "Загружается температура...")
                    val temperatureDeferred = async { repository.fetchTemperature() }

                    _weatherState.value = _weatherState.value.copy(loadingProgress = "Загружается влажность...")
                    val humidityDeferred = async { repository.fetchHumidity() }

                    _weatherState.value = _weatherState.value.copy(loadingProgress = "Загружается ветер...")
                    val windSpeedDeferred = async { repository.fetchWindSpeed() }

                    val temperature = temperatureDeferred.await()
                    val humidity = humidityDeferred.await()
                    val windSpeed = windSpeedDeferred.await()

                    _weatherState.value = _weatherState.value.copy(loadingProgress = "Вычисление индекса...")
                    val index = repository.calculateWeatherIndex()

                    _weatherState.value = WeatherData(
                        temperature = temperature,
                        humidity = humidity,
                        windSpeed = windSpeed,
                        isLoading = false,
                        error = null,
                        loadingProgress = "Загрузка завершена!",
                        weatherIndex = index
                    )
                }
            } catch (e: Exception) {
                _weatherState.value = _weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}",
                    loadingProgress = ""
                )
            }
        }
    }

    fun toggleErrorSimulation() {
        repository.toggleErrorSimulation()
    }


    private fun startAutoRefresh() {
        viewModelScope.launch {
            flow {
                while (true) {
                    delay(10000)
                    emit(Unit)
                }
            }.collect {
                loadWeatherData()
            }
        }
    }
}