package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<Weather>
}
