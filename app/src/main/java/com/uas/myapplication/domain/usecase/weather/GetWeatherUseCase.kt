package com.uas.myapplication.domain.usecase.weather

import com.uas.myapplication.domain.model.Weather
import com.uas.myapplication.domain.repository.WeatherRepository

class GetWeatherUseCase(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Result<Weather> {
        return repository.getCurrentWeather(lat, lon)
    }
}
