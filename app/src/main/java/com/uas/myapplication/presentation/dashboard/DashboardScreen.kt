package com.uas.myapplication.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.BarangCard
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.getMahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.dashboard.components.BarangTerbaruHeader
import com.uas.myapplication.presentation.dashboard.components.DashboardHeader
import com.uas.myapplication.presentation.dashboard.components.QuickActionSection
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider
import com.uas.myapplication.presentation.ui.components.OfflineBanner

@Composable
fun DashboardScreen(
    viewModel    : DashboardViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = StringProvider.get(LocalBahasa.current)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.ambilLaporan(context)
    }

    Scaffold(
        bottomBar = {
            CariInBottomNavBar(
                navController = navController,
                items = getMahasiswaBottomNavItems(strings)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (uiState.isOfflineMode) {
                item {
                    OfflineBanner()
                }
            }

            item {
                DashboardHeader(
                    uiState = uiState
                )
            }

            item {
                QuickActionSection(
                    onLaporKehilanganClick = {
                        navController.navigate(
                            Screen.BuatLaporan.createRoute("hilang")
                        )
                    },
                    onLaporTemuanClick = {
                        navController.navigate(
                            Screen.BuatLaporan.createRoute("ditemukan")
                        )
                    }
                )
            }

            item {
                BarangTerbaruHeader(
                    onLihatSemuaClick = {
                        navController.navigate(
                            Screen.Katalog.route
                        )
                    }
                )
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
                }
            }

            items(uiState.daftarLaporan.take(10)) { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {
                        navController.navigate(Screen.DetailBarang.createRoute(laporan.id))
                    }
                )
            }

            if (!uiState.isLoading && uiState.daftarLaporan.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = strings.noItemReportsYet,
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dashboard Screen - Light", heightDp = 900)
@Composable
fun PreviewDashboardScreen() {
    val strings = StringProvider.get("id")
    MyApplicationTheme {
        val dummyLaporan = listOf(
            Laporan(id = "1", namaBarang = "Jam Tangan", lokasi = "Ruang Baca", tanggal = "12 April 2026", statusBarang = StatusBarang.HILANG),
            Laporan(id = "2", namaBarang = "Kunci Motor", lokasi = "Kantin Teknik", tanggal = "11 April 2026", statusBarang = StatusBarang.DITEMUKAN),
            Laporan(id = "3", namaBarang = "Tumbler Pink", lokasi = "Lab Big Data", tanggal = "10 April 2026", statusBarang = StatusBarang.HILANG)
        )
        val dummyState = DashboardUiState(
            daftarLaporan   = dummyLaporan,
            jumlahHilang    = 2,
            jumlahDitemukan = 1,
            isLoading       = false
        )
        Column(modifier = Modifier.fillMaxSize().background(SlateWhite)) {
            DashboardHeader(
                uiState = dummyState
            )

            QuickActionSection(
                onLaporKehilanganClick = {},
                onLaporTemuanClick = {}
            )

            BarangTerbaruHeader(
                onLihatSemuaClick = {}
            )
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(strings.recentItemsHeader, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextMain)
                Text(strings.btnSeeAll, fontFamily = InterFontFamily, fontSize = 13.sp, color = Blue700)
            }
            dummyLaporan.forEach { laporan ->
                BarangCard(laporan = laporan, onClick = {})
            }
        }
    }
}