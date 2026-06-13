package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.Weather

/**
 * Repository interface untuk data cuaca.
 * Implementasi ada di data layer.
 */
interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Result<Weather>
}
