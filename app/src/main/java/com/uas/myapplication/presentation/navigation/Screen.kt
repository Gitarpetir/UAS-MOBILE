package com.uas.myapplication.presentation.navigation

/**
 * Sealed class yang mendefinisikan semua route navigasi di aplikasi Cari.in.
 * Setiap object merepresentasikan satu halaman.
 */
sealed class Screen(val route: String) {

    // =============================================
    // AUTH FLOW
    // =============================================
    data object Onboarding      : Screen("onboarding")
    data object Login           : Screen("login")
    data object Register        : Screen("register")
    data object LengkapiProfil  : Screen("lengkapi_profil")

    // =============================================
    // MAIN FLOW — MAHASISWA
    // =============================================
    data object Dashboard       : Screen("dashboard")
    data object Katalog         : Screen("katalog")
    data object BuatLaporan     : Screen("buat_laporan")
    data object Profil          : Screen("profil")
    data object Laporanku       : Screen("laporanku")

    // Halaman dengan argumen — ID laporan dikirim lewat route
    data object DetailBarang    : Screen("detail_barang/{laporanId}") {
        fun createRoute(laporanId: String) = "detail_barang/$laporanId"
    }

    // Edit laporan — membuka BuatLaporan dengan data yang sudah ada
    data object EditLaporan     : Screen("edit_laporan/{laporanId}") {
        fun createRoute(laporanId: String) = "edit_laporan/$laporanId"
    }

    // =============================================
    // MAIN FLOW — ADMIN
    // =============================================
    data object DashboardAdmin  : Screen("dashboard_admin")
}