package com.uas.myapplication.domain.model

/**
 * Model murni bisnis untuk data pengguna.
 * Tidak boleh mengandung dependency Firebase atau library apapun.
 */
data class User(
    val uid: String = "",
    val namaLengkap: String = "",
    val nim: String = "",
    val email: String = "",
    val nomorWhatsapp: String = "",
    val peran: String = "mahasiswa" // "mahasiswa" atau "admin"
) {
    // Fungsi helper untuk mengecek apakah pengguna adalah admin
    fun isAdmin(): Boolean = peran == "admin"
}