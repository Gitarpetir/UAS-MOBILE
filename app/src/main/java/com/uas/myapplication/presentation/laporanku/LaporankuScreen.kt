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
                            val judulKosong = when (uiState.tabAktif) {
                                TabLaporanku.BARANGKU -> "Belum ada laporan kehilangan"
                                TabLaporanku.TEMUANKU -> "Belum ada laporan temuan"
                                TabLaporanku.KONTRIBUSI -> "Belum ada kontribusi"
                            }

                            val deskripsiKosong = when (uiState.tabAktif) {
                                TabLaporanku.BARANGKU -> "Tekan tombol + Baru untuk membuat laporan kehilangan"
                                TabLaporanku.TEMUANKU -> "Tekan tombol + Baru untuk membuat laporan temuan"
                                TabLaporanku.KONTRIBUSI -> "Kontribusimu akan muncul di sini"
                            }

                            Text(
                                text = judulKosong,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = TextSub
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = deskripsiKosong,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                color = TextHint
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
    tabAktif: TabLaporanku,
    onTabChange: (TabLaporanku) -> Unit
) {

    val tabs = listOf(
        "Barangku",
        "Temuanku",
        "Kontribusi"
    )

    val selectedIndex = when (tabAktif) {
        TabLaporanku.BARANGKU -> 0
        TabLaporanku.TEMUANKU -> 1
        TabLaporanku.KONTRIBUSI -> 2
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Blue700
    ) {

        tabs.forEachIndexed { index, title ->

            Tab(
                selected = selectedIndex == index,
                onClick = {
                    when (index) {
                        0 -> onTabChange(TabLaporanku.BARANGKU)
                        1 -> onTabChange(TabLaporanku.TEMUANKU)
                        2 -> onTabChange(TabLaporanku.KONTRIBUSI)
                    }
                },
                text = {
                    Text(
                        text = title,
                        fontFamily = InterFontFamily,
                        fontWeight = if (selectedIndex == index)
                            FontWeight.SemiBold
                        else
                            FontWeight.Normal
                    )
                }
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
            TabLaporanku(tabAktif = TabLaporanku.BARANGKU, onTabChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            dummyLaporan.forEach { laporan ->
                LaporankuCard(
                    laporan      = laporan,
                    tabAktif     = TabLaporanku.BARANGKU,
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
            TabLaporanku(tabAktif = TabLaporanku.TEMUANKU, onTabChange = {})
            Spacer(modifier = Modifier.height(8.dp))
            dummyLaporan.forEach { laporan ->
                LaporankuCard(
                    laporan      = laporan,
                    tabAktif     = TabLaporanku.TEMUANKU,
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
