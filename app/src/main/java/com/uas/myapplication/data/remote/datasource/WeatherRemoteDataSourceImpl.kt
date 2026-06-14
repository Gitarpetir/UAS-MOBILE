package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.BuildConfig
import com.uas.myapplication.data.remote.dto.WeatherDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException

/**
 * Implementasi pengambilan data cuaca menggunakan OkHttp.
 */
class WeatherRemoteDataSourceImpl(
    private val client: OkHttpClient
) : WeatherRemoteDataSource {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Result<WeatherDto> = withContext(Dispatchers.IO) {
        try {
            val apiKey = BuildConfig.OPENWEATHERMAP_API_KEY
            if (apiKey.isBlank()) {
                return@withContext Result.failure(Exception("API Key OpenWeatherMap tidak ditemukan."))
            }

            val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=$apiKey&units=metric&lang=id"

            val request = Request.Builder()
                .url(url)
                .get()
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Gagal mengambil data cuaca: ${response.code}"))
            }

            val bodyString = response.body?.string()
                ?: return@withContext Result.failure(Exception("Response body kosong"))

            val weatherDto = WeatherDto.fromJson(bodyString)
            Result.success(weatherDto)

        } catch (e: IOException) {
            Result.failure(Exception("Kesalahan jaringan: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Terjadi kesalahan: ${e.message}"))
        }
    }
}
