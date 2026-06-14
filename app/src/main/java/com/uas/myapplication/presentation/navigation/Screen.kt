    package com.uas.myapplication.presentation.navigation

    sealed class Screen(val route: String) {

        data object Onboarding      : Screen("onboarding")
        data object Login           : Screen("login")
        data object Register        : Screen("register")
        data object LengkapiProfil  : Screen("lengkapi_profil")

        data object Dashboard       : Screen("dashboard")
        data object Katalog         : Screen("katalog")
        data object BuatLaporan : Screen("buat_laporan?status={status}") {
            fun createRoute(status: String): String {
                return "buat_laporan?status=$status"
            }
        }
        data object Profil          : Screen("profil")
        data object Laporanku       : Screen("laporanku")

        data object DetailBarang    : Screen("detail_barang/{laporanId}") {
            fun createRoute(laporanId: String) = "detail_barang/$laporanId"
        }

        data object EditLaporan     : Screen("edit_laporan/{laporanId}") {
            fun createRoute(laporanId: String) = "edit_laporan/$laporanId"
        }

        data object DashboardAdmin  : Screen("dashboard_admin")
        data object KatalogAdmin    : Screen("katalog_admin")
        data object ProfilAdmin     : Screen("profil_admin")

    }