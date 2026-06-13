package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.WeatherDto

/**
 * DataSource interface untuk mengambil data cuaca dari OpenWeatherMap.
 */
interface WeatherRemoteDataSource {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherDto>
}
