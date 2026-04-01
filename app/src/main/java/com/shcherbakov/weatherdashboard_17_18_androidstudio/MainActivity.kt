package com.shcherbakov.weatherdashboard_17_18_androidstudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton  // Шаг 11
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shcherbakov.weatherdashboard_17_18_androidstudio.ui.theme.WeatherDashboardTheme
import com.shcherbakov.weatherdashboard_17_18_androidstudio.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherDashboardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherDashboardScreen()
                }
            }
        }
    }
}

@Composable
fun WeatherDashboardScreen() {
    val viewModel: WeatherViewModel = viewModel()
    val weatherState by viewModel.weatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Weather Dashboard ⛅",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        WeatherCard(
            emoji = "🌡️",
            title = "Temperature",
            value = weatherState.temperature?.let { "$it°C" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.temperature == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            emoji = "💧",
            title = "Humidity",
            value = weatherState.humidity?.let { "$it%" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.humidity == null
        )

        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            emoji = "🌪️",
            title = "Wind Speed",
            value = weatherState.windSpeed?.let { "$it m/s" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.windSpeed == null
        )


        Spacer(modifier = Modifier.height(16.dp))

        WeatherCard(
            emoji = "📊",
            title = "Weather Index",
            value = weatherState.weatherIndex?.let { "$it" } ?: "—",
            isLoading = weatherState.isLoading && weatherState.weatherIndex == null
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.loadWeatherData() },
            enabled = !weatherState.isLoading
        ) {
            Text("🔄 Refresh Weather")
        }


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { viewModel.toggleErrorSimulation() }
        ) {
            Text("⚠️ Simulate Error")
        }


        if (weatherState.loadingProgress.isNotEmpty()) {
            Text(
                text = weatherState.loadingProgress,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }


        if (weatherState.error != null) {
            Text(
                text = "⚠️ ${weatherState.error}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun WeatherCard(emoji: String, title: String, value: String, isLoading: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = emoji,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp)
                )
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}