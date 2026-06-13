package com.uas.myapplication.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.di.ViewModelFactory
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
import com.uas.myapplication.presentation.profil.ProfilScreen
import com.uas.myapplication.presentation.profil.ProfilViewModel

fun NavGraphBuilder.mainNavGraph(navController: NavHostController) {

    composable(Screen.Dashboard.route) {
        val viewModel: DashboardViewModel = viewModel(
            factory = ViewModelFactory {
                DashboardViewModel(
                    getAllLaporanUseCase = AppContainer.getAllLaporanUseCase,
                    getWeatherUseCase = AppContainer.getWeatherUseCase
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

    composable(
        route = Screen.DetailBarang.route,
        arguments = listOf(
            navArgument("laporanId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
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
        val statusAwal = backStackEntry.arguments?.getString("status") ?: "hilang"
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

    composable(
        route = Screen.EditLaporan.route,
        arguments = listOf(
            navArgument("laporanId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val laporanId = backStackEntry.arguments?.getString("laporanId") ?: ""
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
            navController = navController,
            isAdmin = false
        )
    }
}
