package com.uas.myapplication.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uas.myapplication.data.local.PreferensiManager
import com.uas.myapplication.data.remote.datasource.AuthRemoteDataSource
import com.uas.myapplication.data.remote.datasource.AuthRemoteDataSourceImpl
import com.uas.myapplication.data.remote.datasource.LaporanRemoteDataSource
import com.uas.myapplication.data.remote.datasource.LaporanRemoteDataSourceImpl
import com.uas.myapplication.data.remote.datasource.UserRemoteDataSource
import com.uas.myapplication.data.remote.datasource.UserRemoteDataSourceImpl
import com.uas.myapplication.data.repository.AuthRepositoryImpl
import com.uas.myapplication.data.repository.LaporanRepositoryImpl
import com.uas.myapplication.data.repository.UserRepositoryImpl
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.repository.LaporanRepository
import com.uas.myapplication.domain.repository.UserRepository
import com.uas.myapplication.domain.usecase.auth.LogOutUseCase
import com.uas.myapplication.domain.usecase.auth.LoginUseCase
import com.uas.myapplication.domain.usecase.auth.LoginWithGoogleUseCase
import com.uas.myapplication.domain.usecase.auth.RegisterUseCase
import com.uas.myapplication.domain.usecase.laporan.BuatLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.EditLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.GetAllLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.GetLaporanByIdUseCase
import com.uas.myapplication.domain.usecase.laporan.GetMyLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.HapusLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.KonfirmasiTemuanUseCase
import com.uas.myapplication.domain.usecase.user.GetUserProfileUseCase
import com.uas.myapplication.domain.usecase.user.UpdateUserProfileUseCase

/**
 * DI Container manual untuk aplikasi Cari.in.
 *
 * Hirarki dependensi (Clean Architecture):
 *   Firebase SDK → DataSource → Repository → UseCase → ViewModel
 *
 * AppContainer menginisialisasi semua layer secara lazy.
 */
object AppContainer {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // =============================================
    // FRAMEWORK / SDK INSTANCES
    // =============================================

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    // =============================================
    // DATA SOURCES
    // =============================================

    private val authRemoteDataSource: AuthRemoteDataSource by lazy {
        AuthRemoteDataSourceImpl(
            auth = firebaseAuth,
            context = appContext
        )
    }

    private val userRemoteDataSource: UserRemoteDataSource by lazy {
        UserRemoteDataSourceImpl(
            firestore = firestore
        )
    }

    private val laporanRemoteDataSource: LaporanRemoteDataSource by lazy {
        LaporanRemoteDataSourceImpl(
            firestore = firestore,
            context = appContext
        )
    }

    // =============================================
    // REPOSITORIES
    // =============================================

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            authRemoteDataSource = authRemoteDataSource,
            userRemoteDataSource = userRemoteDataSource
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            userRemoteDataSource = userRemoteDataSource
        )
    }

    val laporanRepository: LaporanRepository by lazy {
        LaporanRepositoryImpl(
            laporanRemoteDataSource = laporanRemoteDataSource
        )
    }

    // =============================================
    // USE CASES — Auth
    // =============================================

    val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }

    val loginWithGoogleUseCase: LoginWithGoogleUseCase by lazy {
        LoginWithGoogleUseCase(authRepository)
    }

    val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }

    val logOutUseCase: LogOutUseCase by lazy {
        LogOutUseCase(authRepository)
    }

    // =============================================
    // USE CASES — Laporan
    // =============================================

    val buatLaporanUseCase: BuatLaporanUseCase by lazy {
        BuatLaporanUseCase(laporanRepository)
    }

    val editLaporanUseCase: EditLaporanUseCase by lazy {
        EditLaporanUseCase(laporanRepository)
    }

    val hapusLaporanUseCase: HapusLaporanUseCase by lazy {
        HapusLaporanUseCase(laporanRepository)
    }

    val getAllLaporanUseCase: GetAllLaporanUseCase by lazy {
        GetAllLaporanUseCase(laporanRepository)
    }

    val getLaporanByIdUseCase: GetLaporanByIdUseCase by lazy {
        GetLaporanByIdUseCase(laporanRepository)
    }

    val getMyLaporanUseCase: GetMyLaporanUseCase by lazy {
        GetMyLaporanUseCase(laporanRepository)
    }

    val konfirmasiTemuanUseCase: KonfirmasiTemuanUseCase by lazy {
        KonfirmasiTemuanUseCase(laporanRepository)
    }

    // =============================================
    // USE CASES — User
    // =============================================

    val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(userRepository)
    }

    val updateUserProfileUseCase: UpdateUserProfileUseCase by lazy {
        UpdateUserProfileUseCase(userRepository)
    }

    // =============================================
    // PREFERENCES
    // =============================================

    val preferensiManager: PreferensiManager by lazy {
        PreferensiManager(appContext)
    }
}