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
import com.uas.myapplication.data.remote.datasource.WeatherRemoteDataSource
import com.uas.myapplication.data.remote.datasource.WeatherRemoteDataSourceImpl
import com.uas.myapplication.data.repository.WeatherRepositoryImpl
import com.uas.myapplication.domain.repository.WeatherRepository
import com.uas.myapplication.domain.usecase.weather.GetWeatherUseCase
import okhttp3.OkHttpClient

object AppContainer {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }


    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient()
    }


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

    private val weatherRemoteDataSource: WeatherRemoteDataSource by lazy {
        WeatherRemoteDataSourceImpl(client = okHttpClient)
    }


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
            laporanRemoteDataSource = laporanRemoteDataSource,
            laporanDao = com.uas.myapplication.data.local.database.AppDatabase.getInstance(appContext).laporanDao(),
            context = appContext
        )
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(weatherRemoteDataSource)
    }


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


    val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(userRepository)
    }

    val updateUserProfileUseCase: UpdateUserProfileUseCase by lazy {
        UpdateUserProfileUseCase(userRepository)
    }


    val getWeatherUseCase: GetWeatherUseCase by lazy {
        GetWeatherUseCase(weatherRepository)
    }


    val preferensiManager: PreferensiManager by lazy {
        PreferensiManager(appContext)
    }
}