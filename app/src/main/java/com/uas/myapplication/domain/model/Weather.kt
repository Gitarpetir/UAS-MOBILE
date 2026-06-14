package com.uas.myapplication.domain.model

data class Weather(
    val suhu: Double = 0.0,
    val deskripsi: String = "",
    val iconCode: String = "",
    val namaKota: String = "",
    val kelembapan: Int = 0,
    val angin: Double = 0.0
) {
    fun getEmoji(): String = when {
        iconCode.startsWith("01") -> "☀️"
        iconCode.startsWith("02") -> "⛅"
        iconCode.startsWith("03") || iconCode.startsWith("04") -> "☁️"
        iconCode.startsWith("09") || iconCode.startsWith("10") -> "🌧️"
        iconCode.startsWith("11") -> "⛈️"
        iconCode.startsWith("13") -> "❄️"
        iconCode.startsWith("50") -> "🌫️"
        else -> "🌤️"
    }

    fun getPesanKontekstual(bahasa: String = "id"): String {
        if (bahasa == "en") return getPesanKontekstualEn()
        return when {
            iconCode.startsWith("09") || iconCode.startsWith("10") ->
                "Hujan hari ini! Pastikan barangmu tidak tertinggal 🌧️"
            iconCode.startsWith("11") ->
                "Cuaca buruk! Simpan barang berharga dengan aman ⛈️"
            iconCode.startsWith("01") ->
                "Cuaca cerah! Tetap waspada dengan barang-barangmu ☀️"
            iconCode.startsWith("13") ->
                "Cuaca dingin! Jangan lupa cek barang sebelum pulang ❄️"
            iconCode.startsWith("50") ->
                "Kabut tebal! Hati-hati dan jaga barangmu 🌫️"
            else ->
                "Jangan lupa cek barangmu sebelum meninggalkan ruangan 📋"
        }
    }

    private fun getPesanKontekstualEn(): String = when {
        iconCode.startsWith("09") || iconCode.startsWith("10") ->
            "Rainy today! Make sure you don't leave your belongings behind 🌧️"
        iconCode.startsWith("11") ->
            "Bad weather! Keep your valuables safe ⛈️"
        iconCode.startsWith("01") ->
            "Clear skies! Stay alert with your belongings ☀️"
        iconCode.startsWith("13") ->
            "Cold weather! Don't forget to check your items before leaving ❄️"
        iconCode.startsWith("50") ->
            "Heavy fog! Be careful and guard your belongings 🌫️"
        else ->
            "Don't forget to check your items before leaving the room 📋"
    }
}
