package com.uas.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.uas.myapplication.di.AppContainer
import com.uas.myapplication.presentation.navigation.CariInNavGraph
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppContainer.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            val isDarkMode by AppContainer.preferensiManager.isDarkMode
                .collectAsState(initial = systemDarkTheme)
            val bahasa by AppContainer.preferensiManager.bahasa
                .collectAsState(initial = "id")

            CompositionLocalProvider(LocalBahasa provides bahasa) {
                MyApplicationTheme(darkTheme = isDarkMode) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CariInNavGraph()
                    }
                }
            }
        }
    }
}