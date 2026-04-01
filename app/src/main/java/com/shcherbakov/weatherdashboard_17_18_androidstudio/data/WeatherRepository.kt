package com.shcherbakov.weatherdashboard_17_18_androidstudio.data

import kotlinx.coroutines.delay
import kotlin.random.Random

class WeatherRepository {

    suspend fun fetchTemperature(): Int {
        delay(2000)
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
}