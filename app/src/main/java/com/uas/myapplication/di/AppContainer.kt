package com.uas.myapplication.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.uas.myapplication.data.local.PreferensiManager
import com.uas.myapplication.data.repository.AuthRepositoryImpl
import com.uas.myapplication.data.repository.LaporanRepositoryImpl
import com.uas.myapplication.data.repository.UserRepositoryImpl
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.repository.LaporanRepository
import com.uas.myapplication.domain.repository.UserRepository

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

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(
            auth = firebaseAuth,
            firestore = firestore,
            context = appContext
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            firestore = firestore
        )
    }

    val laporanRepository: LaporanRepository by lazy {
        LaporanRepositoryImpl(
            firestore = firestore,
            context = appContext
        )
    }

    val preferensiManager: PreferensiManager by lazy {
        PreferensiManager(appContext)
    }
}