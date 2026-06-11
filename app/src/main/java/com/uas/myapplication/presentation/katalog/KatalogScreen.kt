package com.uas.myapplication.presentation.katalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
// Import komponen dari ui/components
import com.uas.myapplication.presentation.ui.components.BarangCard
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.katalog.components.FilterChipRow
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.components.mahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.CariInTheme

@Composable
fun KatalogScreen(
    viewModel    : KatalogViewModel,
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
            item {
                Text(
                    text       = "Katalog Barang",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 22.sp,
                    color      = MaterialTheme.colorScheme.onBackground,
                    modifier   = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp)
                )
            }

            item {
                OutlinedTextField(
                    value         = uiState.queryPencarian,
                    onValueChange = viewModel::onQueryPencarianChange,
                    placeholder   = {
                        Text("Cari berdasarkan nama atau deskripsi...", fontFamily = InterFontFamily, fontSize = 13.sp, color = CariInTheme.colors.textHint)
                    },
                    leadingIcon = { Icon(Icons.Default.Search, null, tint = CariInTheme.colors.textHint) },
                    singleLine  = true,
                    modifier    = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape       = RoundedCornerShape(12.dp),
                    colors      = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor   = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor          = MaterialTheme.colorScheme.primary
                    )
                )
            }

            item {
                FilterChipRow(
                    filterAktif    = uiState.filterAktif,
                    onFilterChange = viewModel::onFilterChange
                )
            }

            if (uiState.isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Pakai BarangCard dari ui/components
            items(uiState.laporanFilter) { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {
                        navController.navigate(Screen.DetailBarang.createRoute(laporan.id))
                    }
                )
            }

            if (!uiState.isLoading && uiState.laporanFilter.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(48.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Tidak ada barang ditemukan", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Coba ubah kata kunci atau filter", fontFamily = InterFontFamily, fontSize = 13.sp, color = CariInTheme.colors.textHint)
                        }
                    }
                }
            }
        }
    }
}



// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Katalog Screen - Light", heightDp = 900)
@Composable
fun PreviewKatalogScreen() {
    MyApplicationTheme {
        val dummyLaporan = listOf(
            Laporan(id = "1", namaBarang = "Jam Tangan", lokasi = "Ruang Baca", tanggal = "12 April 2026", statusBarang = StatusBarang.HILANG),
            Laporan(id = "2", namaBarang = "Kunci Motor", lokasi = "Kantin Teknik", tanggal = "11 April 2026", statusBarang = StatusBarang.DITEMUKAN),
            Laporan(id = "3", namaBarang = "Tumbler Pink", lokasi = "Lab Big Data", tanggal = "10 April 2026", statusBarang = StatusBarang.HILANG)
        )
        Column(modifier = Modifier.fillMaxSize().background(SlateWhite)) {
            Text("Katalog Barang", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain, modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 12.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Cari berdasarkan nama atau deskripsi...", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextHint) }, leadingIcon = { Icon(Icons.Default.Search, null, tint = TextHint) }, singleLine = true, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Blue700, unfocusedBorderColor = SlateGray200))
            FilterChipRow(filterAktif = FilterKatalog.SEMUA, onFilterChange = {})
            dummyLaporan.forEach { laporan -> BarangCard(laporan = laporan, onClick = {}) }
        }
    }
}

@Preview(showBackground = true, name = "Filter Chip Row")
@Composable
fun PreviewFilterChipRow() {
    MyApplicationTheme {
        FilterChipRow(filterAktif = FilterKatalog.HILANG, onFilterChange = {})
    }
}