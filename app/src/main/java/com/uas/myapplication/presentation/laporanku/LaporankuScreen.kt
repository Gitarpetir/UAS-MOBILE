package com.uas.myapplication.presentation.laporanku

import com.uas.myapplication.presentation.ui.components.LaporankuCard
import com.uas.myapplication.presentation.ui.components.HapusDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.laporanku.components.EmptyStateLaporanku
import com.uas.myapplication.presentation.laporanku.components.JumlahLaporanText
import com.uas.myapplication.presentation.laporanku.components.LaporankuHeader
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.getMahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider
import com.uas.myapplication.presentation.ui.components.OfflineBanner

@Composable
fun LaporankuScreen(
    viewModel    : LaporankuViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val laporanByTab = viewModel.getLaporanByTab()
    val strings = StringProvider.get(LocalBahasa.current)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadData(context)
    }

    if (uiState.showHapusDialog) {
        HapusDialog(
            namaBarang = uiState.laporanYangDihapus?.namaBarang ?: "",
            onKonfirmasi = viewModel::onKonfirmasiHapus,
            onBatal      = viewModel::onBatalHapus
        )
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

                LaporankuHeader(
                    onTambahClick = {
                        navController.navigate(
                            Screen.BuatLaporan.route
                        )
                    }
                )
            }

            // Jumlah laporan
            item {

                JumlahLaporanText(
                    jumlah = viewModel.getJumlahLaporan()
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            // Tab Barangmu / Temuanmu
            item {
                TabLaporanku(
                    tabAktif    = uiState.tabAktif,
                    onTabChange = viewModel::onTabChange
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Loading
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
                }
            }

            // List laporan
            items(laporanByTab) { laporan ->
                LaporankuCard(
                    laporan = laporan,
                    tabAktif = uiState.tabAktif,

                    bolehEditHapus =
                        uiState.tabAktif != TabLaporanku.KONTRIBUSI,

                    onEditClick = {
                        navController.navigate(
                            Screen.EditLaporan.createRoute(laporan.id)
                        )
                    },

                    onHapusClick = {
                        viewModel.onHapusClick(laporan)
                    }
                )
            }

            if (!uiState.isLoading && laporanByTab.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {

                        EmptyStateLaporanku(
                            tabAktif = uiState.tabAktif
                        )
                    }
                }
            }
        }
    }
}
