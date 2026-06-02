package com.uas.myapplication.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.uas.myapplication.presentation.auth.login.LoginScreen
import com.uas.myapplication.presentation.auth.login.LoginViewModel
import com.uas.myapplication.presentation.onboarding.OnboardingScreen

/**
 * NavGraph utama aplikasi Cari.in.
 * Semua perpindahan halaman diatur di sini.
 *
 * Logika startDestination:
 * - Jika pengguna sudah login → langsung ke Dashboard / Dashboard Admin
 * - Jika belum login → ke Onboarding
 */
@Composable
fun CariInNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Cek apakah pengguna sudah login
    val currentUser = FirebaseAuth.getInstance().currentUser
    val startDestination = if (currentUser != null) {
        Screen.Dashboard.route
    } else {
        Screen.Onboarding.route
    }

    NavHost(
        navController    = navController,
        startDestination = startDestination,
        modifier         = modifier
    ) {

        // =============================================
        // AUTH FLOW
        // =============================================

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onSelesai = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
        //    val viewModel = remember {
        //        LoginViewModel(authRepository = AppContainer.authRepository)
        //    }
        //    LoginScreen(
        //        viewModel      = viewModel,
        //        onLoginSuccess = { isAdmin ->
        //            val target = if (isAdmin) Screen.DashboardAdmin.route
        //            else Screen.Dashboard.route
        //            navController.navigate(target) {
        //                popUpTo(Screen.Login.route) { inclusive = true }
        //            }
        //        },
        //        onDaftarClick = {
        //            navController.navigate(Screen.Register.route)
        //        }
        //    )
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("Login")
            }
        }

        composable(Screen.Register.route) {
            // TODO: hubungkan ke AppContainer setelah DI selesai
            // RegisterScreen(
            //     viewModel         = RegisterViewModel(AppContainer.authRepository),
            //     onRegisterSuccess = { navController.navigate(Screen.Dashboard.route) {
            //         popUpTo(Screen.Register.route) { inclusive = true }
            //     }},
            //     onGoogleSuccess   = { navController.navigate(Screen.LengkapiProfil.route) },
            //     onSudahPunyaAkun  = { navController.popBackStack() }
            // )
            Box(Modifier.fillMaxSize(), Alignment.Center) { Text("Register") }
        }

        composable(Screen.LengkapiProfil.route) {
            // TODO: Ganti dengan LengkapiProfilScreen() di Langkah 6
            // LengkapiProfilScreen(
            //     onSimpanSuccess = { navController.navigate(Screen.Dashboard.route) {
            //         popUpTo(Screen.LengkapiProfil.route) { inclusive = true }
            //     }}
            // )
        }

        // =============================================
        // MAIN FLOW — MAHASISWA
        // =============================================

        composable(Screen.Dashboard.route) {
            // TODO: Ganti dengan DashboardScreen() di Langkah 7
            // DashboardScreen(
            //     onLihatSemuaClick      = { navController.navigate(Screen.Katalog.route) },
            //     onLaporKehilanganClick = { navController.navigate(Screen.BuatLaporan.route) },
            //     onLaporTemuanClick     = { navController.navigate(Screen.BuatLaporan.route) },
            //     onBarangClick          = { id -> navController.navigate(Screen.DetailBarang.createRoute(id)) },
            //     onProfilClick          = { navController.navigate(Screen.Profil.route) },
            //     onLaporankuClick       = { navController.navigate(Screen.Laporanku.route) }
            // )
        }

        composable(Screen.Katalog.route) {
            // TODO: Ganti dengan KatalogScreen() di Langkah 8
            // KatalogScreen(
            //     onBarangClick = { id -> navController.navigate(Screen.DetailBarang.createRoute(id)) },
            //     onBackClick   = { navController.popBackStack() }
            // )
        }

        // Detail Barang — menerima argumen laporanId
        composable(
            route = Screen.DetailBarang.route,
            arguments = listOf(
                navArgument("laporanId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
            // TODO: Ganti dengan DetailBarangScreen() di Langkah 8
            // DetailBarangScreen(
            //     laporanId   = laporanId,
            //     onBackClick = { navController.popBackStack() }
            // )
        }

        composable(Screen.BuatLaporan.route) {
            // TODO: Ganti dengan BuatLaporanScreen() di Langkah 9
            // BuatLaporanScreen(
            //     onKirimSuccess = { navController.popBackStack() },
            //     onBackClick    = { navController.popBackStack() }
            // )
        }

        // Edit Laporan — menerima argumen laporanId
        composable(
            route = Screen.EditLaporan.route,
            arguments = listOf(
                navArgument("laporanId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
            // TODO: Ganti dengan BuatLaporanScreen() mode edit di Langkah 9
            // BuatLaporanScreen(
            //     laporanId      = laporanId,    // mode edit jika tidak null
            //     onKirimSuccess = { navController.popBackStack() },
            //     onBackClick    = { navController.popBackStack() }
            // )
        }

        composable(Screen.Laporanku.route) {
            // TODO: Ganti dengan LaporankuScreen() di Langkah 9
            // LaporankuScreen(
            //     onTambahClick  = { navController.navigate(Screen.BuatLaporan.route) },
            //     onEditClick    = { id -> navController.navigate(Screen.EditLaporan.createRoute(id)) },
            //     onBarangClick  = { id -> navController.navigate(Screen.DetailBarang.createRoute(id)) },
            //     onBackClick    = { navController.popBackStack() }
            // )
        }

        composable(Screen.Profil.route) {
            // TODO: Ganti dengan ProfilScreen() di Langkah 10
            // ProfilScreen(
            //     onLogoutSuccess = { navController.navigate(Screen.Login.route) {
            //         popUpTo(0) { inclusive = true }
            //     }}
            // )
        }

        // =============================================
        // MAIN FLOW — ADMIN
        // =============================================

        composable(Screen.DashboardAdmin.route) {
            // TODO: Ganti dengan DashboardAdminScreen() di Langkah 10
            // DashboardAdminScreen(
            //     onBarangClick   = { id -> navController.navigate(Screen.DetailBarang.createRoute(id)) },
            //     onLogoutSuccess = { navController.navigate(Screen.Login.route) {
            //         popUpTo(0) { inclusive = true }
            //     }}
            // )
        }
    }
}