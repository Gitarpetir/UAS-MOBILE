package com.uas.myapplication.presentation.laporanku

import com.uas.myapplication.presentation.ui.components.LaporankuCard
import com.uas.myapplication.presentation.ui.components.HapusDialog
import com.uas.myapplication.presentation.ui.components.BadgeDeskriptif
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.mahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*


@Composable
fun LaporankuScreen(
    viewModel    : LaporankuViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val laporanByTab = viewModel.getLaporanByTab()

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
                items = mahasiswaBottomNavItems
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
            item {
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Laporanku",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 22.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Button(
                        onClick  = { navController.navigate(Screen.BuatLaporan.route) },
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.buttonColors(containerColor = Blue700),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text       = "Baru",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 13.sp,
                            color      = Color.White
                        )
                    }
                }
            }

            // Jumlah laporan
            item {
                Text(
                    text       = "${viewModel.getJumlahLaporan()} laporan",
                    fontFamily = InterFontFamily,
                    fontSize   = 13.sp,
                    color      = TextSub,
                    modifier   = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                    ) { CircularProgressIndicator(color = Blue700) }
                }
            }

            // List laporan
            items(laporanByTab) { laporan ->
                LaporankuCard(
                    laporan = laporan,
                    tabAktif = uiState.tabAktif,

                    bolehEditHapus =
                        laporan.idPelapor ==
                                viewModel.getCurrentUserId(),

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

            // Tampilan kosong
            if (!uiState.isLoading && laporanByTab.isEmpty()) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector        = Icons.Default.Inbox,
                                contentDescription = null,
                                tint               = TextHint,
                                modifier           = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text       = "Belum ada laporan",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize   = 15.sp,
                                color      = TextSub
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text       = "Tekan tombol + Baru untuk membuat laporan",
                                fontFamily = InterFontFamily,
                                fontSize   = 13.sp,
                                color      = TextHint
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================
// TAB BARANGMU / TEMUANMU
// =============================================
@Composable
fun TabLaporanku(
    tabAktif   : TabLaporanku,
    onTabChange: (TabLaporanku) -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Tab Barangmu
        Button(
            onClick  = { onTabChange(TabLaporanku.BARANGMU) },
            modifier = Modifier.weight(1f).height(40.dp),
            shape    = RoundedCornerShape(20.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor = if (tabAktif == TabLaporanku.BARANGMU) Blue700
                else MaterialTheme.colorScheme.surface,
                contentColor   = if (tabAktif == TabLaporanku.BARANGMU) Color.White
                else TextSub
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text       = "Barangmu",
                fontFamily = InterFontFamily,
                fontWeight = if (tabAktif == TabLaporanku.BARANGMU)
                    FontWeight.SemiBold else FontWeight.Normal,
                fontSize   = 13.sp
            )
        }

        // Tab Temuanmu
        Button(
            onClick  = { onTabChange(TabLaporanku.TEMUANMU) },
            modifier = Modifier.weight(1f).height(40.dp),
            shape    = RoundedCornerShape(20.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor = if (tabAktif == TabLaporanku.TEMUANMU) Blue700
                else MaterialTheme.colorScheme.surface,
                contentColor   = if (tabAktif == TabLaporanku.TEMUANMU) Color.White
                else TextSub
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text       = "Temuanmu",
                fontFamily = InterFontFamily,
                fontWeight = if (tabAktif == TabLaporanku.TEMUANMU)
                    FontWeight.SemiBold else FontWeight.Normal,
                fontSize   = 13.sp
            )
        }
    }
}


@Preview(showBackground = true, name = "Laporanku - Tab Barangmu", heightDp = 800)
@Composable
fun PreviewLaporankuBarangmu() {
    MyApplicationTheme {
        val dummyLaporan = listOf(
            Laporan(id = "1", namaBarang = "Jam Tangan", lokasi = "Ruang Baca", tanggal = "12 April 2026", statusBarang = StatusBarang.HILANG),
            Laporan(id = "2", namaBarang = "Tumbler Pink", lokasi = "Lab Big Data", tanggal = "10 April 2026", statusBarang = StatusBarang.DITEMUKAN)
        )
        Column(
            modifier = Modifier.fillMaxSize().background(SlateWhite)
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Laporanku", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain)
                Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Baru", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.White)
                }
            }
            Text("2 laporan", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextSub, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))
            TabLaporanku(tabAktif = TabLaporanku.BARANGMU, onTabChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            dummyLaporan.forEach { laporan ->
                LaporankuCard(
                    laporan      = laporan,
                    tabAktif     = TabLaporanku.BARANGMU,
                    onEditClick  = {},
                    onHapusClick = {},
                    bolehEditHapus = true
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Laporanku - Tab Temuanmu", heightDp = 800)
@Composable
fun PreviewLaporankuTemuanmu() {
    MyApplicationTheme {
        val dummyLaporan = listOf(
            Laporan(id = "1", namaBarang = "Jam Tangan", lokasi = "Ruang Baca", tanggal = "12 April 2026", statusBarang = StatusBarang.DITEMUKAN),
            Laporan(id = "2", namaBarang = "Tumbler Pink", lokasi = "Lab Big Data", tanggal = "10 April 2026", statusBarang = StatusBarang.DITEMUKAN)
        )
        Column(modifier = Modifier.fillMaxSize().background(SlateWhite)) {
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Laporanku", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain)
                Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700), contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Baru", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.White)
                }
            }
            Text("2 laporan", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextSub, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(12.dp))
            TabLaporanku(tabAktif = TabLaporanku.TEMUANMU, onTabChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            dummyLaporan.forEach { laporan ->
                LaporankuCard(
                    laporan      = laporan,
                    tabAktif     = TabLaporanku.TEMUANMU,
                    onEditClick  = {},
                    onHapusClick = {},
                    bolehEditHapus = false
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Dialog Hapus")
@Composable
fun PreviewHapusDialog() {
    MyApplicationTheme {
        HapusDialog(
            namaBarang   = "Jam Tangan Casio",
            onKonfirmasi = {},
            onBatal      = {}
        )
    }
}

@Preview(showBackground = true, name = "Badge Deskriptif - Semua")
@Composable
fun PreviewBadgeDeskriptif() {
    MyApplicationTheme {
        Column(
            modifier              = Modifier.padding(16.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.HILANG),    tabAktif = TabLaporanku.BARANGMU)
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.DITEMUKAN), tabAktif = TabLaporanku.BARANGMU)
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.DITEMUKAN), tabAktif = TabLaporanku.TEMUANMU)
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.SELESAI),   tabAktif = TabLaporanku.TEMUANMU)
        }
    }
}
