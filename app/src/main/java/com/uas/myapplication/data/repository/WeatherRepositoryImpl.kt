package com.uas.myapplication.data.repository

import com.uas.myapplication.data.remote.datasource.WeatherRemoteDataSource
import com.uas.myapplication.domain.model.Weather
import com.uas.myapplication.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<Weather> {
        return weatherRemoteDataSource.getCurrentWeather(lat, lon).map { weatherDto ->
            weatherDto.toDomain()
        }
    }
}
