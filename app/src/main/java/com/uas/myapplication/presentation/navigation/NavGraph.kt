package com.uas.myapplication.presentation.navigation

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
import com.google.firebase.auth.FirebaseAuth
import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.di.ViewModelFactory
import com.uas.myapplication.presentation.admin.DashboardAdminScreen
import com.uas.myapplication.presentation.admin.DashboardAdminViewModel
import com.uas.myapplication.presentation.admin.LaporanAdminScreen
import com.uas.myapplication.presentation.admin.LaporanAdminViewModel
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

            val viewModel: LoginViewModel = viewModel(
                factory = ViewModelFactory {
                    LoginViewModel(
                        loginUseCase = AppContainer.loginUseCase,
                        loginWithGoogleUseCase = AppContainer.loginWithGoogleUseCase
                    )
                }
            )

            LoginScreen(
                viewModel = viewModel,

                onLoginSuccess = { isAdmin ->

                    val target =
                        if (isAdmin)
                            Screen.DashboardAdmin.route
                        else
                            Screen.Dashboard.route

                    navController.navigate(target) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },

                onDaftarClick = {
                    navController.navigate(Screen.Register.route)
                },

                onLengkapiProfil = {
                    navController.navigate(Screen.LengkapiProfil.route)
                },
            )
        }

        composable(Screen.Register.route) {

            val viewModel: RegisterViewModel = viewModel(
                factory = ViewModelFactory {
                    RegisterViewModel(
                        registerUseCase = AppContainer.registerUseCase,
                        loginWithGoogleUseCase = AppContainer.loginWithGoogleUseCase
                    )
                }
            )

            RegisterScreen(
                viewModel = viewModel,

                onRegisterSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Register.route) {
                            inclusive = true
                        }
                    }
                },

                onGoogleSuccess = {
                    navController.navigate(Screen.LengkapiProfil.route)
                },

                onSudahPunyaAkun = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.LengkapiProfil.route) {

            val viewModel: LengkapiProfilViewModel = viewModel(
                factory = ViewModelFactory {
                    LengkapiProfilViewModel(
                        authRepository = AppContainer.authRepository,
                        updateUserProfileUseCase = AppContainer.updateUserProfileUseCase
                    )
                }
            )

            LengkapiProfilScreen(
                viewModel = viewModel,
                onSimpanSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.LengkapiProfil.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // =============================================
        // MAIN FLOW — MAHASISWA
        // =============================================

        composable(Screen.Dashboard.route) {

            val viewModel: DashboardViewModel = viewModel(
                factory = ViewModelFactory {
                    DashboardViewModel(
                        getAllLaporanUseCase = AppContainer.getAllLaporanUseCase
                    )
                }
            )

            DashboardScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.Katalog.route) {

            val viewModel: KatalogViewModel = viewModel(
                factory = ViewModelFactory {
                    KatalogViewModel(
                        getAllLaporanUseCase = AppContainer.getAllLaporanUseCase
                    )
                }
            )

            KatalogScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // Detail Barang — menerima argumen laporanId
        composable(
            route = Screen.DetailBarang.route,
            arguments = listOf(
                navArgument("laporanId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val laporanId =
                backStackEntry.arguments?.getString("laporanId") ?: ""

            val viewModel: DetailBarangViewModel = viewModel(
                factory = ViewModelFactory {
                    DetailBarangViewModel(
                        getLaporanByIdUseCase = AppContainer.getLaporanByIdUseCase,
                        getUserProfileUseCase = AppContainer.getUserProfileUseCase,
                        konfirmasiTemuanUseCase = AppContainer.konfirmasiTemuanUseCase,
                        authRepository = AppContainer.authRepository
                    )
                }
            )

            DetailBarangScreen(
                viewModel = viewModel,
                laporanId = laporanId,
                navController = navController
            )
        }

        composable(
            route = Screen.BuatLaporan.route,
            arguments = listOf(
                navArgument("status") {
                    type = NavType.StringType
                    defaultValue = "hilang"
                }
            )
        ) { backStackEntry ->

            val statusAwal =
                backStackEntry.arguments?.getString("status")
                    ?: "hilang"

            val viewModel: BuatLaporanViewModel = viewModel(
                factory = ViewModelFactory {
                    BuatLaporanViewModel(
                        buatLaporanUseCase = AppContainer.buatLaporanUseCase,
                        editLaporanUseCase = AppContainer.editLaporanUseCase,
                        getLaporanByIdUseCase = AppContainer.getLaporanByIdUseCase,
                        getUserProfileUseCase = AppContainer.getUserProfileUseCase,
                        authRepository = AppContainer.authRepository
                    )
                }
            )

            BuatLaporanScreen(
                viewModel = viewModel,
                statusAwal = statusAwal,
                navController = navController
            )
        }

        // Edit Laporan — menerima argumen laporanId
        composable(
            route = Screen.EditLaporan.route,
            arguments = listOf(
                navArgument("laporanId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val laporanId =
                backStackEntry.arguments?.getString("laporanId") ?: ""

            val viewModel: BuatLaporanViewModel = viewModel(
                factory = ViewModelFactory {
                    BuatLaporanViewModel(
                        buatLaporanUseCase = AppContainer.buatLaporanUseCase,
                        editLaporanUseCase = AppContainer.editLaporanUseCase,
                        getLaporanByIdUseCase = AppContainer.getLaporanByIdUseCase,
                        getUserProfileUseCase = AppContainer.getUserProfileUseCase,
                        authRepository = AppContainer.authRepository
                    )
                }
            )

            BuatLaporanScreen(
                viewModel = viewModel,
                laporanId = laporanId,
                navController = navController
            )
        }

        composable(Screen.Laporanku.route) {

            val viewModel: LaporankuViewModel = viewModel(
                factory = ViewModelFactory {
                    LaporankuViewModel(
                        getAllLaporanUseCase = AppContainer.getAllLaporanUseCase,
                        hapusLaporanUseCase = AppContainer.hapusLaporanUseCase,
                        authRepository = AppContainer.authRepository
                    )
                }
            )

            LaporankuScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.Profil.route) {

            val viewModel: ProfilViewModel = viewModel(
                factory = ViewModelFactory {
                    ProfilViewModel(
                        authRepository = AppContainer.authRepository,
                        getUserProfileUseCase = AppContainer.getUserProfileUseCase,
                        logOutUseCase = AppContainer.logOutUseCase,
                        preferensiManager = AppContainer.preferensiManager
                    )
                }
            )

            ProfilScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        // =============================================
        // MAIN FLOW — ADMIN
        // =============================================

        composable(Screen.DashboardAdmin.route) {

            val viewModel: DashboardAdminViewModel = viewModel(
                factory = ViewModelFactory {
                    DashboardAdminViewModel(
                        getAllLaporanUseCase = AppContainer.getAllLaporanUseCase
                    )
                }
            )

            DashboardAdminScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(Screen.LaporanAdmin.route) {

            val viewModel: LaporanAdminViewModel = viewModel(
                factory = ViewModelFactory {
                    LaporanAdminViewModel(
                        getAllLaporanUseCase = AppContainer.getAllLaporanUseCase
                    )
                }
            )

            LaporanAdminScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}