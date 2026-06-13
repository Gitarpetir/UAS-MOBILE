package com.uas.myapplication.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.di.ViewModelFactory
import com.uas.myapplication.presentation.admin.dashboard.DashboardAdminScreen
import com.uas.myapplication.presentation.admin.dashboard.DashboardAdminViewModel
import com.uas.myapplication.presentation.admin.katalog.KatalogAdminScreen
import com.uas.myapplication.presentation.admin.katalog.KatalogAdminViewModel
import com.uas.myapplication.presentation.auth.lengkapi_profil.LengkapiProfilScreen
import com.uas.myapplication.presentation.auth.lengkapi_profil.LengkapiProfilViewModel
import com.uas.myapplication.presentation.auth.login.LoginScreen
import com.uas.myapplication.presentation.auth.login.LoginViewModel
import com.uas.myapplication.presentation.auth.register.RegisterScreen
import com.uas.myapplication.presentation.auth.register.RegisterViewModel
import com.uas.myapplication.presentation.dashboard.DashboardScreen
import com.uas.myapplication.presentation.dashboard.DashboardViewModel
import com.uas.myapplication.presentation.detail.DetailBarangScreen
import com.uas.myapplication.presentation.detail.DetailBarangViewModel
import com.uas.myapplication.presentation.katalog.KatalogScreen
import com.uas.myapplication.presentation.katalog.KatalogViewModel
import com.uas.myapplication.presentation.laporan.BuatLaporanScreen
import com.uas.myapplication.presentation.laporan.BuatLaporanViewModel
import com.uas.myapplication.presentation.laporanku.LaporankuScreen
import com.uas.myapplication.presentation.laporanku.LaporankuViewModel
import com.uas.myapplication.presentation.onboarding.OnboardingScreen
import com.uas.myapplication.presentation.profil.ProfilScreen
import com.uas.myapplication.presentation.profil.ProfilViewModel

/**
 * NavGraph utama aplikasi Cari.in.
 * Semua perpindahan halaman diatur di sini.
 *
 * Logika startDestination:
 * - Jika pengguna sudah login → langsung ke Dashboard / Dashboard Admin
 * - Jika belum login → ke Onboarding
 *
 * Dependensi ViewModel sekarang menggunakan Use Cases dari AppContainer
 * alih-alih Repository langsung (sesuai prinsip Clean Architecture).
 */
@Composable
fun CariInNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Cek apakah pengguna sudah login
    val currentUserId = AppContainer.authRepository.getCurrentUserId()
    val startDestination = "auth_check"

    NavHost(
        navController    = navController,
        startDestination = startDestination,
        modifier         = modifier,
        enterTransition = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) },
        exitTransition = { androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) },
        popEnterTransition = { androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) },
        popExitTransition = { androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) }
    ) {

        // =============================================
        // AUTH CHECK FLOW
        // =============================================
        
        composable("auth_check") {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                if (currentUserId == null) {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(0)
                    }
                } else {
                    val result = AppContainer.getUserProfileUseCase(currentUserId)
                    val isAdmin = result.getOrNull()?.isAdmin() == true
                    if (isAdmin) {
                        navController.navigate(Screen.DashboardAdmin.route) {
                            popUpTo(0)
                        }
                    } else {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(0)
                        }
                    }
                }
            }
            
            Box(
                modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.CircularProgressIndicator(color = androidx.compose.material3.MaterialTheme.colorScheme.primary)
            }
        }

        // =============================================
        // MODULAR NAVIGATION GRAPHS
        // =============================================
        
        authNavGraph(navController)
        mainNavGraph(navController)
        adminNavGraph(navController)
    }
}