package com.shcherbakov.weatherdashboard_17_18_androidstudio.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class WeatherRepository {


    var simulateError = false

    fun toggleErrorSimulation() {
        simulateError = !simulateError
    }

    suspend fun fetchTemperature(): Int {
        delay(2000)
        if (simulateError) throw Exception("Сервер недоступен")
        return Random.nextInt(15, 35)
    }

    suspend fun fetchHumidity(): Int {
        delay(1500)
        return Random.nextInt(30, 80)
    }

    suspend fun fetchWindSpeed(): Int {
        delay(1000)
        return Random.nextInt(0, 20)
    }


    suspend fun calculateWeatherIndex(): Int {
        return withContext(Dispatchers.Default) {
            var sum = 0
            for (i in 1..1000000) {
                sum += i
            }
            Random.nextInt(0, 100)
        }
    }
}