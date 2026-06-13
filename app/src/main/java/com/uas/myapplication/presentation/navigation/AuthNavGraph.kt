package com.uas.myapplication.presentation.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.di.ViewModelFactory
import com.uas.myapplication.presentation.auth.lengkapi_profil.LengkapiProfilScreen
import com.uas.myapplication.presentation.auth.lengkapi_profil.LengkapiProfilViewModel
import com.uas.myapplication.presentation.auth.login.LoginScreen
import com.uas.myapplication.presentation.auth.login.LoginViewModel
import com.uas.myapplication.presentation.auth.register.RegisterScreen
import com.uas.myapplication.presentation.auth.register.RegisterViewModel
import com.uas.myapplication.presentation.onboarding.OnboardingScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {

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
                val target = if (isAdmin) Screen.DashboardAdmin.route else Screen.Dashboard.route
                navController.navigate(target) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            },
            onDaftarClick = {
                navController.navigate(Screen.Register.route)
            },
            onLengkapiProfil = {
                navController.navigate(Screen.LengkapiProfil.route)
            }
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
                    popUpTo(Screen.Register.route) { inclusive = true }
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
                    popUpTo(Screen.LengkapiProfil.route) { inclusive = true }
                }
            }
        )
    }
}
