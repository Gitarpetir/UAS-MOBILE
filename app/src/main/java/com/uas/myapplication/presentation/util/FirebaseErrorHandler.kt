package com.uas.myapplication.presentation.util

fun parseFirebaseError(message: String?): String {
    if (message == null) return "Terjadi kesalahan, coba lagi"
    
    val lowerMessage = message.lowercase()
    
    return when {
        lowerMessage.contains("password") -> "Password salah atau terlalu lemah"
        lowerMessage.contains("no user record") -> "Email tidak terdaftar"
        lowerMessage.contains("email address") -> "Email sudah terdaftar"
        lowerMessage.contains("badly formatted") -> "Format email tidak valid"
        lowerMessage.contains("network") -> "Tidak ada koneksi internet"
        lowerMessage.contains("blocked") -> "Terlalu banyak percobaan, coba lagi nanti"
        lowerMessage.contains("weak-password") -> "Password terlalu lemah"
        else -> "Autentikasi gagal, silakan periksa kembali data Anda"
    }
}
