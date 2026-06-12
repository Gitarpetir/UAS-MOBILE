package com.uas.myapplication.presentation.admin.katalog

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
import com.uas.myapplication.presentation.admin.components.FilterChipAdminRow
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import androidx.compose.ui.tooling.preview.Preview
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang

@Composable
fun KatalogAdminScreen(
    viewModel: KatalogAdminViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)
    val context = androidx.compose.ui.platform.LocalContext.current

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.ambilSemuaLaporan(context)
    }

    Scaffold(
        bottomBar = {
            CariInBottomNavBar(
                navController = navController,
                items = com.uas.myapplication.presentation.ui.components.getAdminBottomNavItems(strings)
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
                    com.uas.myapplication.presentation.ui.components.OfflineBanner()
                }
            }

            item {
                Text(
                    text = strings.manageReportsHeader,
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
                            text = strings.searchPlaceholder,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            color = CariInTheme.colors.textHint
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = CariInTheme.colors.textHint
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary
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
                            color = MaterialTheme.colorScheme.primary
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
                                text = strings.noItemsFound,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = strings.tryChangingFilter,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                color = CariInTheme.colors.textHint
                            )
                        }
                    }
                }
            }
        }
    }
}



@Preview(
    showBackground = true,
    name = "Katalog Admin",
    heightDp = 900
)
@Composable
fun PreviewKatalogAdminScreen() {

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