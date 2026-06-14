package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.WeatherDto

interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherDto>
}
