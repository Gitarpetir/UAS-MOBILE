package com.uas.myapplication.presentation.admin

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
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
import com.uas.myapplication.presentation.ui.components.StatusBadge
import com.uas.myapplication.presentation.ui.theme.*

// =============================================
// WARNA HIGHLIGHT PERINGATAN (barang lama)
// =============================================
private val WarnYellow      = Color(0xFFF59E0B)
private val WarnYellowLight = Color(0xFFFFFBEB)
private val WarnYellowBorder= Color(0xFFFCD34D)

// =============================================
// HALAMAN UTAMA
// =============================================

@Composable
fun DashboardAdminScreen(
    viewModel    : DashboardAdminViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Navigasi ke Login setelah logout
    var logoutTriggered by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost    = { SnackbarHost(snackbarHostState) },
        containerColor  = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // =============================================
            // HEADER BIRU
            // =============================================
            HeaderAdmin(
                namaAdmin      = uiState.namaAdmin,
                totalHilang    = uiState.totalHilang,
                totalDitemukan = uiState.totalDitemukan,
                totalSelesai   = uiState.totalSelesai,
                onLogout       = {
                    viewModel.onLogout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )

            // =============================================
            // FILTER CHIP
            // =============================================
            FilterChipRowAdmin(
                filterAktif = uiState.filterAktif,
                onFilterPilih = viewModel::onFilterPilih
            )

            // =============================================
            // SORT + JUMLAH DATA
            // =============================================
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text       = "${uiState.laporanTerfilter.size} laporan",
                    fontFamily = InterFontFamily,
                    fontSize   = 13.sp,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SortToggleButton(
                    sortAktif    = uiState.sortAktif,
                    onSortPilih  = viewModel::onSortPilih
                )
            }

            // Keterangan highlight kuning
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(WarnYellow, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text       = "Barang ditemukan > 3 hari belum diselesaikan",
                    fontFamily = InterFontFamily,
                    fontSize   = 11.sp,
                    color      = WarnYellow
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // =============================================
            // DAFTAR LAPORAN
            // =============================================
            when {
                uiState.isLoading -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Blue700)
                    }
                }

                uiState.laporanTerfilter.isEmpty() -> {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector        = Icons.Default.Inbox,
                                contentDescription = null,
                                tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier           = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text       = "Tidak ada laporan",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 16.sp,
                                color      = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding    = PaddingValues(bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = uiState.laporanTerfilter,
                            key   = { it.id }
                        ) { laporan ->
                            AdminBarangCard(
                                laporan      = laporan,
                                isLama       = viewModel.isBarangLama(laporan),
                                onClick      = {
                                    navController.navigate(
                                        Screen.DetailBarang.createRoute(laporan.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// =============================================
// HEADER ADMIN
// =============================================

@Composable
private fun HeaderAdmin(
    namaAdmin     : String,
    totalHilang   : Int,
    totalDitemukan: Int,
    totalSelesai  : Int,
    onLogout      : () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Blue700,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .padding(top = 20.dp, bottom = 20.dp, start = 16.dp, end = 16.dp)
    ) {
        Column {
            // Baris judul + tombol logout
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text       = "Dashboard Admin",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = White
                    )
                    if (namaAdmin.isNotEmpty()) {
                        Text(
                            text       = "Halo, $namaAdmin",
                            fontFamily = InterFontFamily,
                            fontSize   = 13.sp,
                            color      = White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Tombol logout di pojok kanan atas header
                IconButton(
                    onClick  = onLogout,
                    modifier = Modifier
                        .background(White.copy(alpha = 0.15f), CircleShape)
                        .size(40.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Default.Logout,
                        contentDescription = "Logout",
                        tint               = White,
                        modifier           = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3 card statistik
            Row(
                modifier            = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    label  = "Hilang",
                    jumlah = totalHilang,
                    warna  = DangerRed,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label  = "Ditemukan",
                    jumlah = totalDitemukan,
                    warna  = SuccessGreen,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    label  = "Selesai",
                    jumlah = totalSelesai,
                    warna  = NeutralGray,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    label   : String,
    jumlah  : Int,
    warna   : Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = White.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier            = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text       = jumlah.toString(),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = White
            )
            Text(
                text       = label,
                fontFamily = InterFontFamily,
                fontSize   = 12.sp,
                color      = White.copy(alpha = 0.85f)
            )
        }
    }
}

// =============================================
// FILTER CHIP ROW
// =============================================

@Composable
private fun FilterChipRowAdmin(
    filterAktif  : FilterAdmin,
    onFilterPilih: (FilterAdmin) -> Unit
) {
    val pilihan = listOf(
        FilterAdmin.SEMUA     to "Semua",
        FilterAdmin.HILANG    to "Hilang",
        FilterAdmin.DITEMUKAN to "Ditemukan",
        FilterAdmin.SELESAI   to "Selesai"
    )

    LazyRow(
        modifier            = Modifier.padding(vertical = 10.dp),
        contentPadding      = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pilihan) { (filter, label) ->
            val isAktif = filterAktif == filter
            val bgColor by animateColorAsState(
                targetValue = if (isAktif) Blue700 else MaterialTheme.colorScheme.surface,
                animationSpec = tween(200),
                label = "filterColor"
            )
            val textColor by animateColorAsState(
                targetValue = if (isAktif) White else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(200),
                label = "filterTextColor"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .border(
                        width = 1.dp,
                        color = if (isAktif) Blue700 else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onFilterPilih(filter) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text       = label,
                    fontFamily = InterFontFamily,
                    fontWeight = if (isAktif) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize   = 13.sp,
                    color      = textColor
                )
            }
        }
    }
}

// =============================================
// SORT TOGGLE BUTTON
// =============================================

@Composable
private fun SortToggleButton(
    sortAktif  : SortAdmin,
    onSortPilih: (SortAdmin) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = Icons.Default.Sort,
                contentDescription = "Sort",
                tint               = Blue700,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text       = if (sortAktif == SortAdmin.TERBARU) "Terbaru" else "Terlama",
                fontFamily = InterFontFamily,
                fontSize   = 13.sp,
                color      = Blue700,
                fontWeight = FontWeight.Medium
            )
            Icon(
                imageVector        = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint               = Blue700,
                modifier           = Modifier.size(16.dp)
            )
        }

        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text    = {
                    Text(
                        "Terbaru",
                        fontFamily = InterFontFamily,
                        fontWeight = if (sortAktif == SortAdmin.TERBARU) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                onClick = {
                    onSortPilih(SortAdmin.TERBARU)
                    expanded = false
                },
                leadingIcon = {
                    if (sortAktif == SortAdmin.TERBARU) {
                        Icon(Icons.Default.Check, null, tint = Blue700, modifier = Modifier.size(16.dp))
                    }
                }
            )
            DropdownMenuItem(
                text    = {
                    Text(
                        "Terlama",
                        fontFamily = InterFontFamily,
                        fontWeight = if (sortAktif == SortAdmin.TERLAMA) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                onClick = {
                    onSortPilih(SortAdmin.TERLAMA)
                    expanded = false
                },
                leadingIcon = {
                    if (sortAktif == SortAdmin.TERLAMA) {
                        Icon(Icons.Default.Check, null, tint = Blue700, modifier = Modifier.size(16.dp))
                    }
                }
            )
        }
    }
}

// =============================================
// CARD BARANG (ADMIN VARIANT)
// =============================================

@Composable
private fun AdminBarangCard(
    laporan : Laporan,
    isLama  : Boolean,
    onClick : () -> Unit
) {
    // Animasi warna border/background untuk highlight
    val borderColor by animateColorAsState(
        targetValue   = if (isLama) WarnYellowBorder else Color.Transparent,
        animationSpec = tween(300),
        label         = "borderColor"
    )
    val bgColor by animateColorAsState(
        targetValue   = if (isLama) WarnYellowLight else MaterialTheme.colorScheme.surface,
        animationSpec = tween(300),
        label         = "bgColor"
    )

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(
                width = if (isLama) 1.5.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto barang
            AsyncImage(
                model              = laporan.fotoUrl.ifEmpty { null },
                contentDescription = "Foto ${laporan.namaBarang}",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SlateGray100)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info barang
            Column(modifier = Modifier.weight(1f)) {
                // Nama + ikon peringatan jika lama
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text       = laporan.namaBarang,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onSurface,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.weight(1f)
                    )
                    if (isLama) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector        = Icons.Default.Warning,
                            contentDescription = "Barang sudah lama",
                            tint               = WarnYellow,
                            modifier           = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Nama pelapor
                Text(
                    text       = laporan.namaPelapor,
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Lokasi
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier           = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text       = laporan.lokasi,
                        fontFamily = InterFontFamily,
                        fontSize   = 12.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Tanggal
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier           = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text       = laporan.tanggal,
                        fontFamily = InterFontFamily,
                        fontSize   = 12.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Badge status
            StatusBadge(status = laporan.statusBarang)
        }
    }
}

// =============================================
// PREVIEW
// =============================================

@Preview(showBackground = true, name = "Dashboard Admin - Light", heightDp = 900)
@Composable
fun PreviewDashboardAdmin() {
    MyApplicationTheme {
        val dummyList = listOf(
            Laporan(
                id           = "1",
                namaBarang   = "Jam Tangan Rolex",
                namaPelapor  = "Muhammad Alfi",
                lokasi       = "FT ULM BJM",
                tanggal      = "12 April 2026",
                statusBarang = StatusBarang.HILANG,
                waktuDibuat  = System.currentTimeMillis()
            ),
            Laporan(
                id           = "2",
                namaBarang   = "Kunci Motor",
                namaPelapor  = "Budi Santoso",
                lokasi       = "Lab Big Data",
                tanggal      = "9 April 2026",
                statusBarang = StatusBarang.DITEMUKAN,
                // Simulasi barang lama (4 hari lalu)
                waktuDibuat  = System.currentTimeMillis() - (4 * 24 * 60 * 60 * 1000L)
            ),
            Laporan(
                id           = "3",
                namaBarang   = "Dompet Hitam",
                namaPelapor  = "Siti Rahma",
                lokasi       = "Perpustakaan",
                tanggal      = "8 April 2026",
                statusBarang = StatusBarang.SELESAI,
                waktuDibuat  = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000L)
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
        ) {
            // Preview Header
            HeaderAdmin(
                namaAdmin      = "Admin FT ULM",
                totalHilang    = 3,
                totalDitemukan = 5,
                totalSelesai   = 12,
                onLogout       = {}
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Preview Filter Chips (statis)
            FilterChipRowAdmin(
                filterAktif   = FilterAdmin.SEMUA,
                onFilterPilih = {}
            )

            // Preview Sort + counter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "3 laporan",
                    fontFamily = InterFontFamily,
                    fontSize   = 13.sp,
                    color      = TextSub
                )
                SortToggleButton(sortAktif = SortAdmin.TERBARU, onSortPilih = {})
            }

            Row(
                modifier          = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(10.dp).background(WarnYellow, CircleShape))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Barang ditemukan > 3 hari belum diselesaikan",
                    fontFamily = InterFontFamily,
                    fontSize   = 11.sp,
                    color      = WarnYellow
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Preview Card list
            dummyList.forEach { laporan ->
                Spacer(modifier = Modifier.height(8.dp))
                val isLama = laporan.statusBarang == StatusBarang.DITEMUKAN &&
                        (System.currentTimeMillis() - laporan.waktuDibuat) > (3 * 24 * 60 * 60 * 1000L)
                AdminBarangCard(
                    laporan = laporan,
                    isLama  = isLama,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Admin Card - Normal")
@Composable
fun PreviewAdminCardNormal() {
    MyApplicationTheme {
        AdminBarangCard(
            laporan = Laporan(
                id           = "1",
                namaBarang   = "Laptop Asus VivoBook",
                namaPelapor  = "Rizki Ramadhan",
                lokasi       = "Ruang Kelas 3.4",
                tanggal      = "11 April 2026",
                statusBarang = StatusBarang.HILANG
            ),
            isLama  = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Admin Card - Highlight Lama")
@Composable
fun PreviewAdminCardLama() {
    MyApplicationTheme {
        AdminBarangCard(
            laporan = Laporan(
                id           = "2",
                namaBarang   = "Kunci Motor Yamaha",
                namaPelapor  = "Dewi Kurnia",
                lokasi       = "Lab Big Data",
                tanggal      = "7 April 2026",
                statusBarang = StatusBarang.DITEMUKAN
            ),
            isLama  = true, // ← highlight kuning aktif
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Dashboard Admin - Dark", heightDp = 900)
@Composable
fun PreviewDashboardAdminDark() {
    MyApplicationTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            HeaderAdmin(
                namaAdmin      = "Admin FT ULM",
                totalHilang    = 3,
                totalDitemukan = 5,
                totalSelesai   = 12,
                onLogout       = {}
            )
        }
    }
}