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
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.getMahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import java.util.Calendar
import androidx.core.net.toUri
import com.uas.myapplication.presentation.laporan.components.FormHeader
import com.uas.myapplication.presentation.laporan.components.LabelWajib
import com.uas.myapplication.presentation.laporan.components.StatusBarangSelector
import com.uas.myapplication.presentation.laporan.components.SubmitSection
import com.uas.myapplication.presentation.laporan.components.TeksError
import com.uas.myapplication.presentation.laporan.components.FotoPicker
import com.uas.myapplication.presentation.laporan.components.TanggalField
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanScreen(
    viewModel: BuatLaporanViewModel,
    statusAwal: String = "hilang",
    laporanId: String? = null,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = StringProvider.get(LocalBahasa.current)

    LaunchedEffect(statusAwal) {

        if (laporanId == null) {
            viewModel.inisialisasiBuatBaru(statusAwal)
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
                        val hari = calendar.get(Calendar.DAY_OF_MONTH)
                        val bulan = listOf(
                            "Januari",
                            "Februari",
                            "Maret",
                            "April",
                            "Mei",
                            "Juni",
                            "Juli",
                            "Agustus",
                            "September",
                            "Oktober",
                            "November",
                            "Desember"
                        )[calendar.get(Calendar.MONTH)]
                        val tahun = calendar.get(Calendar.YEAR)
                        viewModel.onTanggalChange("$hari $bulan $tahun")
                    }
                    showDatePicker = false
                }) { Text(strings.btnSelect, color = MaterialTheme.colorScheme.primary, fontFamily = InterFontFamily) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(strings.btnCancel, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = InterFontFamily)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Blue700,
                    todayDateBorderColor = Blue700,
                    selectedYearContainerColor = Blue700
                )
            )
        }
    }

    val isEditMode = !laporanId.isNullOrEmpty()

    Scaffold(
        bottomBar = {
            if (!isEditMode) {
                CariInBottomNavBar(
                    navController = navController,
                    items = getMahasiswaBottomNavItems(strings)
                )
            }
        }
    ) { paddingValues ->
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
                title = if (isEditMode) "Edit Barang" else strings.reportItemTitle,
                onBackClick = if (isEditMode) { { navController.popBackStack() } } else null
            )

            Spacer(modifier = Modifier.height(20.dp))

            StatusBarangSelector(
                selectedStatus = uiState.statusBarang,
                onStatusChange = viewModel::onStatusBarangChange
            )

            Spacer(modifier = Modifier.height(20.dp))

            FotoPicker(
                fotoUri = uiState.fotoUri,
                fotoUrl = uiState.fotoUrl,
                onClick = {
                    fotoLauncher.launch("image/*")
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            LabelWajib(strings.itemNameLabel)

            CariInTextField(
                value = uiState.namaBarang,
                onValueChange = viewModel::onNamaBarangChange,
                label = "",
                placeholder = strings.itemNamePlaceholder,
                isError = uiState.namaBarangError
            )

            if (uiState.namaBarangError) {
                TeksError(strings.itemNameRequired)
            }
            Spacer(modifier = Modifier.height(16.dp))

            LabelWajib(strings.descriptionLabel)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = uiState.deskripsi,
                onValueChange = viewModel::onDeskripsiChange,
                placeholder = {
                    Text(
                        strings.descriptionPlaceholder,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        color = TextHint
                    )
                },
                modifier = Modifier.fillMaxWidth().height(120.dp),
                shape = RoundedCornerShape(12.dp),
                isError = uiState.deskripsiError,
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Blue700,
                    unfocusedBorderColor = SlateGray200,
                    cursorColor = Blue700,
                    errorBorderColor = DangerRed
                )
            )
            if (uiState.deskripsiError) TeksError(strings.descriptionRequired)

            Spacer(modifier = Modifier.height(16.dp))

            LabelWajib(strings.locationLabel)
            CariInTextField(
                value = uiState.lokasi,
                onValueChange = viewModel::onLokasiChange,
                label = "",
                placeholder = strings.locationPlaceholder,
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = TextHint) },
                isError = uiState.lokasiError
            )
            if (uiState.lokasiError) TeksError(strings.locationRequired)

            Spacer(modifier = Modifier.height(16.dp))

            TanggalField(
                tanggal = uiState.tanggal,
                isError = uiState.tanggalError,
                onClick = {
                    showDatePicker = true
                }
            )

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = uiState.errorMessage ?: "",
                    color = DangerRed,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            SubmitSection(
                isLoading = uiState.isLoading,
                isEditMode = uiState.isEditMode,
                onSubmit = viewModel::kirimLaporan
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
}}
