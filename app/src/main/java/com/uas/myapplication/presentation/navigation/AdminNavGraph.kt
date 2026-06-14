package com.uas.myapplication.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.di.ViewModelFactory
import com.uas.myapplication.presentation.admin.dashboard.DashboardAdminScreen
import com.uas.myapplication.presentation.admin.dashboard.DashboardAdminViewModel
import com.uas.myapplication.presentation.admin.katalog.KatalogAdminScreen
import com.uas.myapplication.presentation.admin.katalog.KatalogAdminViewModel
import com.uas.myapplication.presentation.profil.ProfilScreen
import com.uas.myapplication.presentation.profil.ProfilViewModel

fun NavGraphBuilder.adminNavGraph(navController: NavHostController) {

    composable(Screen.DashboardAdmin.route) {
        val viewModel: DashboardAdminViewModel = viewModel(
            factory = ViewModelFactory {
                DashboardAdminViewModel(
                    getAllLaporanUseCase = AppContainer.getAllLaporanUseCase,
                    getWeatherUseCase = AppContainer.getWeatherUseCase
                )
            }
        )

        DashboardAdminScreen(
            viewModel = viewModel,
            navController = navController
        )
    }

    composable(Screen.KatalogAdmin.route) {
        val viewModel: KatalogAdminViewModel = viewModel(
            factory = ViewModelFactory {
                KatalogAdminViewModel(
                    getAllLaporanUseCase = AppContainer.getAllLaporanUseCase,
                    hapusLaporanUseCase = AppContainer.hapusLaporanUseCase
                )
            }
        )

        KatalogAdminScreen(
            viewModel = viewModel,
            navController = navController
        )
    }

    composable(Screen.ProfilAdmin.route) {
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
            isAdmin = true
        )
    }
}
