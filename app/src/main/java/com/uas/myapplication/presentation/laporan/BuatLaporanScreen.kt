package com.uas.myapplication.presentation.laporan

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.auth.login.CariInTextField
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.mahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import java.util.Calendar
import androidx.core.net.toUri
import com.uas.myapplication.presentation.laporan.components.FormHeader
import com.uas.myapplication.presentation.laporan.components.LabelWajib
import com.uas.myapplication.presentation.laporan.components.StatusBarangSelector
import com.uas.myapplication.presentation.laporan.components.SubmitSection
import com.uas.myapplication.presentation.laporan.components.TeksError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanScreen(
    viewModel: BuatLaporanViewModel,
    statusAwal: String = "hilang",
    laporanId: String? = null,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(statusAwal) {

        if (laporanId == null) {

            viewModel.onStatusBarangChange(
                if (statusAwal == "ditemukan")
                    StatusBarang.DITEMUKAN
                else
                    StatusBarang.HILANG
            )
        }
    }

    LaunchedEffect(laporanId) {
        if (!laporanId.isNullOrEmpty()) {
            viewModel.inisialisasiUntukEdit(laporanId)
        }
    }

    LaunchedEffect(uiState.kirimSuccess) {
        if (uiState.kirimSuccess) {
            navController.popBackStack()
            viewModel.resetKirimSuccess()
        }
    }

    val fotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onFotoUriChange(it.toString()) }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val calendar = Calendar.getInstance()
                        calendar.timeInMillis = millis
                        val hari  = calendar.get(Calendar.DAY_OF_MONTH)
                        val bulan = listOf("Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember")[calendar.get(Calendar.MONTH)]
                        val tahun = calendar.get(Calendar.YEAR)
                        viewModel.onTanggalChange("$hari $bulan $tahun")
                    }
                    showDatePicker = false
                }) { Text("Pilih", color = MaterialTheme.colorScheme.primary, fontFamily = InterFontFamily) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = InterFontFamily)
                }
            }
        ) {
            DatePicker(
                state  = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor  = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor       = MaterialTheme.colorScheme.primary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

    Scaffold(bottomBar = {
        CariInBottomNavBar(
            navController = navController,
            items = mahasiswaBottomNavItems
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            FormHeader(
                title = "Laporkan Barang"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Toggle Status Barang
            StatusBarangSelector(
                selectedStatus = uiState.statusBarang,
                onStatusChange = viewModel::onStatusBarangChange
            )
                Button(
                    onClick   = { viewModel.onStatusBarangChange(StatusBarang.DITEMUKAN) },
                    modifier  = Modifier.weight(1f).height(44.dp),
                    shape     = RoundedCornerShape(10.dp),
                    colors    = ButtonDefaults.buttonColors(
                        containerColor = if (uiState.statusBarang == StatusBarang.DITEMUKAN) SuccessGreen else MaterialTheme.colorScheme.surface,
                        contentColor   = if (uiState.statusBarang == StatusBarang.DITEMUKAN) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) { Text("Ditemukan", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Upload Foto
            Text("Foto Barang", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(360.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .clickable { fotoLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                when {
                    uiState.fotoUri.isNotEmpty() -> AsyncImage(model = uiState.fotoUri.toUri(), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    uiState.fotoUrl.isNotEmpty() -> AsyncImage(model = uiState.fotoUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Upload, null, tint = CariInTheme.colors.textHint, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tekan untuk mengunggah foto", fontFamily = InterFontFamily, fontSize = 13.sp, color = CariInTheme.colors.textHint)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Nama Barang
            LabelWajib("Nama Barang")
            Spacer(modifier = Modifier.height(6.dp))
            CariInTextField(value = uiState.namaBarang, onValueChange = viewModel::onNamaBarangChange, label = "", placeholder = "misalnya, Jam Hitam", isError = uiState.namaBarangError)
            if (uiState.namaBarangError) TeksError("Nama barang wajib diisi")

            Spacer(modifier = Modifier.height(16.dp))

            // Deskripsi
            LabelWajib("Deskripsi")
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value         = uiState.deskripsi,
                onValueChange = viewModel::onDeskripsiChange,
                placeholder   = { Text("Berikan detail tentang barang tersebut...", fontFamily = InterFontFamily, fontSize = 14.sp, color = CariInTheme.colors.textHint) },
                modifier      = Modifier.fillMaxWidth().height(120.dp),
                shape         = RoundedCornerShape(12.dp),
                isError       = uiState.deskripsiError,
                maxLines      = 5,
                colors        = OutlinedTextFieldDefaults.colors(focusedBorderColor = MaterialTheme.colorScheme.primary, unfocusedBorderColor = MaterialTheme.colorScheme.outline, cursorColor = MaterialTheme.colorScheme.primary, errorBorderColor = MaterialTheme.colorScheme.error)
            )
            if (uiState.deskripsiError) TeksError("Deskripsi wajib diisi")

            Spacer(modifier = Modifier.height(16.dp))

            // Lokasi
            LabelWajib("Lokasi")
            Spacer(modifier = Modifier.height(6.dp))
            CariInTextField(value = uiState.lokasi, onValueChange = viewModel::onLokasiChange, label = "", placeholder = "misalnya, Lab Komputer Dasar", leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = CariInTheme.colors.textHint) }, isError = uiState.lokasiError)
            if (uiState.lokasiError) TeksError("Lokasi wajib diisi")

            Spacer(modifier = Modifier.height(16.dp))

            // Tanggal — DatePicker
            LabelWajib("Tanggal")
            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = uiState.tanggal,
                    onValueChange = {},
                    placeholder = {
                        Text(
                            "Pilih tanggal",
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            color = CariInTheme.colors.textHint
                        )
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = CariInTheme.colors.textHint
                        )
                    },
                    readOnly = true,
                    isError = uiState.tanggalError,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        errorBorderColor = MaterialTheme.colorScheme.error
                    )
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            showDatePicker = true
                        }
                )
            }

            if (uiState.tanggalError) {
                TeksError("Tanggal wajib dipilih")
            }

            AnimatedVisibility(visible = uiState.errorMessage != null, enter = fadeIn(), exit = fadeOut()) {
                Text(text = uiState.errorMessage ?: "", color = MaterialTheme.colorScheme.error, fontFamily = InterFontFamily, fontSize = 12.sp, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tombol Kirim / Simpan
        SubmitSection(
            isLoading = uiState.isLoading,
            isEditMode = uiState.isEditMode,
            onSubmit = viewModel::kirimLaporan
        )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

// Komponen Label Wajib
// Komponen Teks Error

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Buat Laporan - Mode Baru", heightDp = 1100)
@Composable
fun PreviewBuatLaporan() {
    MyApplicationTheme {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(SlateWhite).padding(horizontal = 16.dp)) {

            Spacer(modifier = Modifier.height(24.dp))

            Text("Laporkan Barang", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain)

            Spacer(modifier = Modifier.height(20.dp))

            Text("Status Barang", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextMain)

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = DangerRed), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Hilang", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White) }
                Button(onClick = {}, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = SlateGray50), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Ditemukan", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub) }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text("Foto Barang", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = TextMain)
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth().
                    height(160.dp).
                    clip(RoundedCornerShape(12.dp)).
                    background(SlateGray50).
                    border(1.5.dp, SlateGray200, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Upload, null, tint = TextHint, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tekan untuk mengunggah foto", fontFamily = InterFontFamily, fontSize = 13.sp, color = TextHint)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LabelWajib("Nama Barang"); Spacer(Modifier.height(6.dp))

            CariInTextField(value = "", onValueChange = {}, label = "", placeholder = "misalnya, Jam Hitam")

            Spacer(modifier = Modifier.height(16.dp))

            LabelWajib("Deskripsi"); Spacer(Modifier.height(6.dp))

            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Berikan detail tentang barang tersebut...", fontFamily = InterFontFamily, fontSize = 14.sp, color = TextHint) }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(12.dp), maxLines = 5, colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Blue700, unfocusedBorderColor = SlateGray200))

            Spacer(modifier = Modifier.height(16.dp))

            LabelWajib("Lokasi"); Spacer(Modifier.height(6.dp))

            CariInTextField(value = "", onValueChange = {}, label = "", placeholder = "misalnya, Lab Komputer Dasar", leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = TextHint) })

            Spacer(modifier = Modifier.height(16.dp))
            LabelWajib("Tanggal"); Spacer(Modifier.height(6.dp))
            OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Pilih tanggal", fontFamily = InterFontFamily, fontSize = 14.sp, color = TextHint) }, leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = TextHint) }, readOnly = true, singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Blue700, unfocusedBorderColor = SlateGray200))
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700)) {
                Icon(Icons.Default.Send, null, tint = Color.White, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp))
                Text("Kirim Laporan", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true, name = "Buat Laporan - Mode Edit", heightDp = 900)
@Composable
fun PreviewEditLaporan() {
    MyApplicationTheme {
        Column(modifier = Modifier.fillMaxSize().background(SlateWhite).padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(24.dp))
            Text("Laporkan Barang", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain)
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = SlateGray50), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Hilang", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = TextSub) }
                Button(onClick = {}, modifier = Modifier.weight(1f).height(44.dp), shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen), elevation = ButtonDefaults.buttonElevation(0.dp)) { Text("Ditemukan", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White) }
            }
            Spacer(modifier = Modifier.height(20.dp))
            CariInTextField(value = "Jam Tangan Casio", onValueChange = {}, label = "Nama Barang", placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "Jam tangan hitam dengan tali kulit coklat", onValueChange = {}, label = "Deskripsi", placeholder = "")
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "Ruang Baca Lt.2", onValueChange = {}, label = "Lokasi", placeholder = "", leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "12 April 2026", onValueChange = {}, label = "Tanggal", placeholder = "", leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700)) {
                Icon(Icons.Default.Save, null, tint = Color.White, modifier = Modifier.size(18.dp)); Spacer(Modifier.width(8.dp))
                Text("Simpan Perubahan", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        }
    }
}