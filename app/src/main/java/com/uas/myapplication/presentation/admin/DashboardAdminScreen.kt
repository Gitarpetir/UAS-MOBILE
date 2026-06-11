package com.uas.myapplication.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.StatistikCard
import com.uas.myapplication.presentation.ui.components.adminBottomNavItems
import com.uas.myapplication.presentation.admin.components.HeaderDashboardAdmin
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun DashboardAdminScreen(
    viewModel: DashboardAdminViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            CariInBottomNavBar(
                navController = navController,
                items = adminBottomNavItems
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
                HeaderDashboardAdmin(uiState)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Laporan Terbaru",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )



                }
            }

            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            items(uiState.daftarLaporan.take(10)) { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {
                        navController.navigate(
                            Screen.DetailBarang.createRoute(laporan.id)
                        )
                    }
                )
            }

            if (!uiState.isLoading && uiState.daftarLaporan.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Belum ada laporan",
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    name = "Dashboard Admin"
)
@Composable
fun PreviewDashboardAdminScreen() {

    MyApplicationTheme {

        val dummyState = DashboardAdminUiState(
            jumlahHilang = 12,
            jumlahDitemukan = 8,
            jumlahSelesai = 21,
            daftarLaporan = listOf(
                Laporan(
                    id = "1",
                    namaBarang = "Kunci Motor",
                    lokasi = "Kantin Teknik",
                    tanggal = "12 Mei 2026",
                    statusBarang = StatusBarang.HILANG
                ),
                Laporan(
                    id = "2",
                    namaBarang = "Dompet Hitam",
                    lokasi = "Perpustakaan",
                    tanggal = "10 Mei 2026",
                    statusBarang = StatusBarang.DITEMUKAN
                )
            ),
            isLoading = false
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
        ) {

            HeaderDashboardAdmin(
                uiState = dummyState
            )

            Text(
                text = "Laporan Terbaru",
                modifier = Modifier.padding(16.dp),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

            dummyState.daftarLaporan.forEach { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {}
                )
            }
        }
    }
}