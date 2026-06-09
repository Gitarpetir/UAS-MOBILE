package com.uas.myapplication.presentation.admin

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.BarangCard
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.CariInFilterChip
import com.uas.myapplication.presentation.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.components.adminBottomNavItems

@Composable
fun LaporanAdminScreen(
    viewModel: LaporanAdminViewModel,
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
                Text(
                    text = "Kelola Laporan",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 24.dp,
                        bottom = 12.dp
                    )
                )
            }

            item {
                OutlinedTextField(
                    value = uiState.queryPencarian,
                    onValueChange = viewModel::onQueryPencarianChange,
                    placeholder = {
                        Text(
                            text = "Cari laporan...",
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            color = TextHint
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextHint
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Blue700,
                        unfocusedBorderColor = SlateGray200,
                        cursorColor = Blue700
                    )
                )
            }

            item {
                FilterChipAdminRow(
                    filterAktif = uiState.filterAktif,
                    onFilterChange = viewModel::onFilterChange
                )
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
                            color = Blue700
                        )
                    }
                }
            }

            items(uiState.laporanFilter) { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {
                        navController.navigate(
                            Screen.DetailBarang.createRoute(laporan.id)
                        )
                    }
                )
            }

            if (!uiState.isLoading && uiState.laporanFilter.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "Tidak ada laporan ditemukan",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = TextSub
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Coba ubah kata kunci atau filter",
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

@Composable
fun FilterChipAdminRow(
    filterAktif: FilterAdmin,
    onFilterChange: (FilterAdmin) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        CariInFilterChip(
            label = "Semua",
            isSelected = filterAktif == FilterAdmin.SEMUA,
            onClick = {
                onFilterChange(FilterAdmin.SEMUA)
            }
        )

        CariInFilterChip(
            label = "Hilang",
            isSelected = filterAktif == FilterAdmin.HILANG,
            onClick = {
                onFilterChange(FilterAdmin.HILANG)
            }
        )

        CariInFilterChip(
            label = "Ditemukan",
            isSelected = filterAktif == FilterAdmin.DITEMUKAN,
            onClick = {
                onFilterChange(FilterAdmin.DITEMUKAN)
            }
        )

        CariInFilterChip(
            label = "Selesai",
            isSelected = filterAktif == FilterAdmin.SELESAI,
            onClick = {
                onFilterChange(FilterAdmin.SELESAI)
            }
        )
    }
}

@Preview(
    showBackground = true,
    name = "Laporan Admin Screen",
    heightDp = 900
)
@Composable
fun PreviewLaporanAdminScreen() {

    MyApplicationTheme {

        val dummyLaporan = listOf(
            Laporan(
                id = "1",
                namaBarang = "Dompet Hitam",
                lokasi = "Perpustakaan",
                tanggal = "12 Mei 2026",
                statusBarang = StatusBarang.HILANG
            ),
            Laporan(
                id = "2",
                namaBarang = "Kunci Motor",
                lokasi = "Kantin Teknik",
                tanggal = "10 Mei 2026",
                statusBarang = StatusBarang.DITEMUKAN
            ),
            Laporan(
                id = "3",
                namaBarang = "Flashdisk Kingston",
                lokasi = "Gedung F",
                tanggal = "8 Mei 2026",
                statusBarang = StatusBarang.SELESAI
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
        ) {

            Text(
                text = "Kelola Laporan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextMain,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 24.dp,
                    bottom = 12.dp
                )
            )

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        text = "Cari laporan...",
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        color = TextHint
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextHint
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue700,
                    unfocusedBorderColor = SlateGray200
                )
            )

            FilterChipAdminRow(
                filterAktif = FilterAdmin.SEMUA,
                onFilterChange = {}
            )

            dummyLaporan.forEach { laporan ->
                BarangCard(
                    laporan = laporan,
                    onClick = {}
                )
            }
        }
    }
}