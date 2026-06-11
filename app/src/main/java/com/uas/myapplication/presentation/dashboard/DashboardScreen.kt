package com.uas.myapplication.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.BarangCard
import com.uas.myapplication.presentation.ui.components.StatistikCard
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.mahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun DashboardScreen(
    viewModel    : DashboardViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
            item { HeaderDashboard(uiState = uiState) }

            item {
                TombolAksiDashboard(
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
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = "Barang Terbaru",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 16.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text     = "Lihat Semua",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color    = Blue700,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Katalog.route)
                        }
                    )
                }
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator(color = Blue700) }
                }
            }

            // Pakai BarangCard dari ui/components
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
                            text       = "Belum ada laporan barang",
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp,
                            color      = TextSub
                        )
                    }
                }
            }
        }
    }
}

// =============================================
// HEADER BIRU
// =============================================
@Composable
fun HeaderDashboard(uiState: DashboardUiState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(colors = listOf(Blue700, Blue800))
            )
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column {
            Text(
                text       = "Cari.in",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah   = uiState.jumlahHilang,
                    label    = "Barang Hilang",
                    warna    = DangerRed
                )
                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah   = uiState.jumlahDitemukan,
                    label    = "Barang Ditemukan",
                    warna    = SuccessGreen
                )
            }
        }
    }
}

// =============================================
// TOMBOL AKSI
// =============================================
@Composable
fun TombolAksiDashboard(
    onLaporKehilanganClick: () -> Unit,
    onLaporTemuanClick    : () -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick  = onLaporKehilanganClick,
            modifier = Modifier.weight(1f).height(56.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text       = "Laporkan\nKehilangan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Color.White
            )
        }
        OutlinedButton(
            onClick  = onLaporTemuanClick,
            modifier = Modifier.weight(1f).height(56.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.5.dp, Blue700),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Blue700)
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = Blue700, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text       = "Laporkan\nTemuan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Blue700
            )
        }
    }
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Dashboard Screen - Light", heightDp = 900)
@Composable
fun PreviewDashboardScreen() {
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
            HeaderDashboard(uiState = dummyState)
            TombolAksiDashboard(onLaporKehilanganClick = {}, onLaporTemuanClick = {})
            Row(
                modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Barang Terbaru", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextMain)
                Text("Lihat Semua", fontFamily = InterFontFamily, fontSize = 13.sp, color = Blue700)
            }
            dummyLaporan.forEach { laporan ->
                BarangCard(laporan = laporan, onClick = {})
            }
        }
    }
}