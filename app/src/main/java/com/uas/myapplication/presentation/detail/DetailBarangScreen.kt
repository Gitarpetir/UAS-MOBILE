package com.uas.myapplication.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.StatusBadge
import com.uas.myapplication.presentation.ui.theme.*

// =============================================
// DETAIL BARANG SCREEN
// =============================================
@Composable
fun DetailBarangScreen(
    viewModel    : DetailBarangViewModel,
    laporanId    : String,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Muat detail saat halaman pertama dibuka
    LaunchedEffect(laporanId) {
        viewModel.muatDetail(laporanId)
    }

    // Kembali setelah aksi berhasil
    LaunchedEffect(uiState.aksiSuccess) {
        if (uiState.aksiSuccess) {
            navController.popBackStack()
            viewModel.resetAksiSuccess()
        }
    }

    // Dialog "Aku Menemukan Barang Ini"
    if (uiState.showDialogTemukan) {
        DialogKonfirmasiTemukan(
            onKonfirmasi = viewModel::onKonfirmasiTemukan,
            onBatal      = viewModel::onBatalDialogTemukan
        )
    }

    // Dialog "Barang Ini Milik Saya"
    if (uiState.showDialogMilik) {
        DialogKonfirmasiMilik(
            onMengerti = viewModel::onBatalDialogMilik
        )
    }

    Scaffold(
        bottomBar = { CariInBottomNavBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // =============================================
            // TOP BAR — panah kembali + judul
            // =============================================
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector        = Icons.Default.ArrowBack,
                        contentDescription = "Kembali",
                        tint               = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text       = "Detail Barang",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 18.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier         = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = Blue700) }
            } else {
                uiState.laporan?.let { laporan ->

                    // =============================================
                    // FOTO BARANG — full width
                    // =============================================
                    AsyncImage(
                        model              = laporan.fotoUrl.ifEmpty { null },
                        contentDescription = "Foto ${laporan.namaBarang}",
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .background(SlateGray100)
                    )

                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        // =============================================
                        // NAMA BARANG + BADGE STATUS
                        // =============================================
                        Row(
                            modifier          = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text       = laporan.namaBarang,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize   = 22.sp,
                                color      = MaterialTheme.colorScheme.onBackground,
                                modifier   = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatusBadge(status = laporan.statusBarang)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Deskripsi
                        Text(
                            text       = laporan.deskripsi,
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp,
                            color      = TextSub,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // =============================================
                        // CARD LOKASI & TANGGAL
                        // =============================================
                        Card(
                            modifier  = Modifier.fillMaxWidth(),
                            shape     = RoundedCornerShape(12.dp),
                            colors    = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text       = "Lokasi  & Tanggal",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 15.sp,
                                    color      = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Lokasi
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector        = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint               = Blue700,
                                        modifier           = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text       = "Lokasi",
                                            fontFamily = InterFontFamily,
                                            fontSize   = 11.sp,
                                            color      = TextSub
                                        )
                                        Text(
                                            text       = laporan.lokasi,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            fontSize   = 14.sp,
                                            color      = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Tanggal
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector        = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint               = Blue700,
                                        modifier           = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text       = "Tanggal",
                                            fontFamily = InterFontFamily,
                                            fontSize   = 11.sp,
                                            color      = TextSub
                                        )
                                        Text(
                                            text       = laporan.tanggal,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            fontSize   = 14.sp,
                                            color      = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // =============================================
                        // CARD INFORMASI PENGGUNA
                        // =============================================
                        Card(
                            modifier  = Modifier.fillMaxWidth(),
                            shape     = RoundedCornerShape(12.dp),
                            colors    = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text       = "Informasi Pengguna",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 15.sp,
                                    color      = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Nama pelapor
                                InfoItem(
                                    icon  = Icons.Default.Person,
                                    label = "Reported by",
                                    value = laporan.namaPelapor
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // NIM
                                InfoItem(
                                    icon  = Icons.Default.Badge,
                                    label = "NIM",
                                    value = laporan.nimPelapor
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // Email
                                InfoItem(
                                    icon  = Icons.Default.Email,
                                    label = "Email",
                                    value = laporan.emailPelapor
                                )

                                // Nomor WhatsApp — hanya tampil untuk Admin
                                if (uiState.isAdmin && laporan.whatsappPelapor.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    InfoItem(
                                        icon  = Icons.Default.Phone,
                                        label = "WhatsApp",
                                        value = laporan.whatsappPelapor
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // =============================================
                        // TOMBOL AKSI — berbeda sesuai status & peran
                        // =============================================

                        // Tombol aksi utama (hanya tampil jika status bukan SELESAI)
                        if (laporan.statusBarang != StatusBarang.SELESAI) {
                            Button(
                                onClick  = viewModel::onTombolAksiClick,
                                enabled  = !uiState.isLoading,
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape    = RoundedCornerShape(16.dp),
                                colors   = ButtonDefaults.buttonColors(
                                    containerColor = Blue700,
                                    disabledContainerColor = Blue700.copy(alpha = 0.7f)
                                )
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(
                                        modifier    = Modifier.size(22.dp),
                                        color       = Color.White,
                                        strokeWidth = 2.5.dp
                                    )
                                } else {
                                    Text(
                                        text = when (laporan.statusBarang) {
                                            StatusBarang.HILANG    -> "Aku Menemukan Barang Ini"
                                            StatusBarang.DITEMUKAN -> "Barang Ini Milik Saya"
                                            else                   -> ""
                                        },
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize   = 15.sp,
                                        color      = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Tombol Admin — Selesaikan Laporan
                        if (uiState.isAdmin) {
                            Button(
                                onClick  = viewModel::onSelesaikanLaporan,
                                enabled  = !uiState.isLoading,
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape    = RoundedCornerShape(16.dp),
                                colors   = ButtonDefaults.buttonColors(
                                    containerColor = SuccessGreen
                                )
                            ) {
                                Icon(
                                    imageVector        = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint               = Color.White,
                                    modifier           = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text       = "Selesaikan Laporan",
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize   = 15.sp,
                                    color      = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        // Tombol Kembali
                        OutlinedButton(
                            onClick  = { navController.popBackStack() },
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape    = RoundedCornerShape(16.dp),
                            border   = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = SlateGray200
                            )
                        ) {
                            Text(
                                text       = "Kembali",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize   = 15.sp,
                                color      = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // Tampilan error
                if (uiState.errorMessage != null) {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = uiState.errorMessage ?: "",
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp,
                            color      = DangerRed
                        )
                    }
                }
            }
        }
    }
}

// =============================================
// KOMPONEN INFO ITEM
// =============================================
@Composable
fun InfoItem(
    icon : androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = Blue700,
            modifier           = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text       = label,
                fontFamily = InterFontFamily,
                fontSize   = 11.sp,
                color      = TextSub
            )
            Text(
                text       = value,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// =============================================
// DIALOG — AKU MENEMUKAN BARANG INI
// =============================================
@Composable
fun DialogKonfirmasiTemukan(
    onKonfirmasi: () -> Unit,
    onBatal     : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onBatal,
        icon = {
            Icon(
                imageVector        = Icons.Default.Search,
                contentDescription = null,
                tint               = Blue700,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Konfirmasi Penemuan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = "Apakah Anda benar menemukan barang ini? Jika ya, mohon segera serahkan barang tersebut ke Ruangan Bersama.",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSub,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onKonfirmasi,
                colors  = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text       = "Ya, Saya Temukan",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onBatal,
                shape   = RoundedCornerShape(8.dp),
                border  = androidx.compose.foundation.BorderStroke(1.dp, SlateGray200)
            ) {
                Text(
                    text       = "Batal",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color      = TextSub
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

// =============================================
// DIALOG — BARANG INI MILIK SAYA
// =============================================
@Composable
fun DialogKonfirmasiMilik(
    onMengerti: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onMengerti,
        icon = {
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = SuccessGreen,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Ambil Barangmu",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = "Silakan menuju Ruang Bersama untuk mengambil barang Anda dengan menunjukkan identitas.",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSub,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onMengerti,
                colors  = ButtonDefaults.buttonColors(containerColor = Blue700),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text       = "Mengerti",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Detail Barang - Hilang", heightDp = 1000)
@Composable
fun PreviewDetailBarangHilang() {
    MyApplicationTheme {
        val dummyLaporan = Laporan(
            id           = "1",
            namaBarang   = "Jam Tangan",
            deskripsi    = "Jam tangan rolex dalamnya hitam dengan emas 24 karat.",
            lokasi       = "FT ULM BJM",
            tanggal      = "12 April 2026",
            statusBarang = StatusBarang.HILANG,
            namaPelapor  = "Muhammad Alfi Gunawan",
            nimPelapor   = "2410817110009",
            emailPelapor = "alfi.gunawan@gmail.com"
        )
        Column(
            modifier = Modifier.fillMaxSize().background(SlateWhite)
        ) {
            Row(
                modifier          = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = TextMain)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Detail Barang", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = TextMain)
            }
            Box(modifier = Modifier.fillMaxWidth().height(240.dp).background(SlateGray100))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(dummyLaporan.namaBarang, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain, modifier = Modifier.weight(1f))
                    StatusBadge(status = dummyLaporan.statusBarang)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(dummyLaporan.deskripsi, fontFamily = InterFontFamily, fontSize = 14.sp, color = TextSub, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Lokasi  & Tanggal", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(Icons.Default.LocationOn, "Lokasi", dummyLaporan.lokasi)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.CalendarToday, "Tanggal", dummyLaporan.tanggal)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Informasi Pengguna", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(Icons.Default.Person, "Reported by", dummyLaporan.namaPelapor)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.Badge, "NIM", dummyLaporan.nimPelapor)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.Email, "Email", dummyLaporan.emailPelapor)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700)) {
                    Text("Aku Menemukan Barang Ini", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), border = androidx.compose.foundation.BorderStroke(1.dp, SlateGray200)) {
                    Text("Kembali", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextMain)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Detail Barang - Ditemukan", heightDp = 1000)
@Composable
fun PreviewDetailBarangDitemukan() {
    MyApplicationTheme {
        val dummyLaporan = Laporan(
            id           = "2",
            namaBarang   = "Kunci Motor",
            deskripsi    = "Kunci motor yamaha jadul nmax kayanya.",
            lokasi       = "Lab Big Data",
            tanggal      = "11 April 2026",
            statusBarang = StatusBarang.DITEMUKAN,
            namaPelapor  = "Muhammad Alfi Gunawan",
            nimPelapor   = "2410817110009",
            emailPelapor = "alfi.gunawan@gmail.com"
        )
        Column(modifier = Modifier.fillMaxSize().background(SlateWhite)) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ArrowBack, null, tint = TextMain)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Detail Barang", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = TextMain)
            }
            Box(modifier = Modifier.fillMaxWidth().height(240.dp).background(SlateGray100))
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(dummyLaporan.namaBarang, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain, modifier = Modifier.weight(1f))
                    StatusBadge(status = dummyLaporan.statusBarang)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(dummyLaporan.deskripsi, fontFamily = InterFontFamily, fontSize = 14.sp, color = TextSub, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Lokasi  & Tanggal", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(Icons.Default.LocationOn, "Lokasi", dummyLaporan.lokasi)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.CalendarToday, "Tanggal", dummyLaporan.tanggal)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Informasi Pengguna", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        InfoItem(Icons.Default.Person, "Reported by", dummyLaporan.namaPelapor)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.Badge, "NIM", dummyLaporan.nimPelapor)
                        Spacer(modifier = Modifier.height(10.dp))
                        InfoItem(Icons.Default.Email, "Email", dummyLaporan.emailPelapor)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700)) {
                    Text("Barang Ini Milik Saya", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), border = androidx.compose.foundation.BorderStroke(1.dp, SlateGray200)) {
                    Text("Kembali", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextMain)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Dialog Konfirmasi Temukan")
@Composable
fun PreviewDialogTemukan() {
    MyApplicationTheme {
        DialogKonfirmasiTemukan(onKonfirmasi = {}, onBatal = {})
    }
}

@Preview(showBackground = true, name = "Dialog Konfirmasi Milik")
@Composable
fun PreviewDialogMilik() {
    MyApplicationTheme {
        DialogKonfirmasiMilik(onMengerti = {})
    }
}