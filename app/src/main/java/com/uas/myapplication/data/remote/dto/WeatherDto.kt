package com.uas.myapplication.data.remote.dto

import com.uas.myapplication.domain.model.Weather
import org.json.JSONObject

/**
 * Data Transfer Object (DTO) untuk response JSON dari OpenWeatherMap.
 * Karena kita mem-parsing secara manual dengan org.json, ini lebih ke
 * data class murni yang siap dipetakan.
 */
data class WeatherDto(
    val temp: Double,
    val description: String,
    val icon: String,
    val cityName: String,
    val humidity: Int,
    val windSpeed: Double
) {
    fun toDomain(): Weather {
        return Weather(
            suhu = temp,
            deskripsi = description,
            iconCode = icon,
            namaKota = cityName,
            kelembapan = humidity,
            angin = windSpeed
        )
    }

    companion object {
        /**
         * Mem-parsing raw JSON String dari OpenWeatherMap menjadi WeatherDto.
         */
        fun fromJson(jsonString: String): WeatherDto {
            val root = JSONObject(jsonString)
            
            val weatherArray = root.getJSONArray("weather")
            var description = ""
            var icon = ""
            if (weatherArray.length() > 0) {
                val weatherObj = weatherArray.getJSONObject(0)
                description = weatherObj.getString("description")
                icon = weatherObj.getString("icon")
            }
            
            val mainObj = root.getJSONObject("main")
            val temp = mainObj.getDouble("temp")
            val humidity = mainObj.getInt("humidity")
            
            val windObj = root.getJSONObject("wind")
            val windSpeed = windObj.getDouble("speed")
            
            val cityName = root.getString("name")
            
            return WeatherDto(
                temp = temp,
                description = description,
                icon = icon,
                cityName = cityName,
                humidity = humidity,
                windSpeed = windSpeed
            )
        }
    }
}
