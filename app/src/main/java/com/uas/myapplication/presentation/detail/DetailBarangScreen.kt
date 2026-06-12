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
import com.uas.myapplication.presentation.detail.components.DetailImage
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.getMahasiswaBottomNavItems
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.detail.components.DetailActionSection
import com.uas.myapplication.presentation.detail.components.DetailHeader
import com.uas.myapplication.presentation.detail.components.DetailInfoSection
import com.uas.myapplication.presentation.detail.components.DialogKonfirmasiMilik
import com.uas.myapplication.presentation.detail.components.DialogKonfirmasiTemukan
import com.uas.myapplication.presentation.detail.components.InformasiPenggunaCard
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun DetailBarangScreen(
    viewModel    : DetailBarangViewModel,
    laporanId    : String,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = StringProvider.get(LocalBahasa.current)

    LaunchedEffect(laporanId) {
        viewModel.muatDetail(laporanId)
    }

    LaunchedEffect(uiState.aksiSuccess) {
        if (uiState.aksiSuccess) {
            navController.popBackStack()
            viewModel.resetAksiSuccess()
        }
    }

    if (uiState.showDialogTemukan) {
        DialogKonfirmasiTemukan(
            onKonfirmasi = viewModel::onKonfirmasiTemukan,
            onBatal      = viewModel::onBatalDialogTemukan
        )
    }

    if (uiState.showDialogMilik) {
        DialogKonfirmasiMilik(
            onKonfirmasi = viewModel::onKonfirmasiMilik,
            onBatal = viewModel::onBatalDialogMilik
        )
    }

    Scaffold(
        // NavBar dihide pada DetailBarang
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            DetailHeader(
                onBackClick = { navController.popBackStack() }
            )

            if (uiState.isLoading) {
                Box(
                    modifier         = Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            } else {
                uiState.laporan?.let { laporan ->

                    DetailImage(
                        fotoUrl = laporan.fotoUrl,
                        namaBarang = laporan.namaBarang
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        DetailInfoSection(
                            laporan = laporan
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        val pelaporTitle = if (laporan.jenisLaporan == com.uas.myapplication.domain.model.JenisLaporan.TEMUAN) {
                            "Informasi Penemu (Pelapor)"
                        } else {
                            "Informasi Pelapor (Pemilik)"
                        }
                        
                        InformasiPenggunaCard(
                            title = pelaporTitle,
                            nama = laporan.namaPelapor,
                            nim = laporan.nimPelapor
                        )

                        if (laporan.idPenemu.isNotBlank() && laporan.idPenemu != laporan.idPelapor) {
                            Spacer(modifier = Modifier.height(12.dp))
                            InformasiPenggunaCard(
                                title = strings.finderInfo,
                                nama = laporan.namaPenemu,
                                nim = laporan.nimPenemu
                            )
                        }

                        if (laporan.idPemilik.isNotBlank() && laporan.idPemilik != laporan.idPelapor) {
                            Spacer(modifier = Modifier.height(12.dp))
                            InformasiPenggunaCard(
                                title = "Informasi Pemilik",
                                nama = laporan.namaPemilik,
                                nim = laporan.nimPemilik
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        
                        DetailActionSection(
                            laporan = laporan,
                            isAdmin = uiState.isAdmin,
                            currentUserId = uiState.currentUser?.uid,
                            isLoading = uiState.isLoading,
                            onAksiClick = viewModel::onTombolAksiClick,
                            onSelesaikan = viewModel::onSelesaikanLaporan,
                            onKembali = { navController.popBackStack() }
                        )
                    }
                }

                if (uiState.errorMessage != null) {
                    Box(
                        modifier         = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text       = uiState.errorMessage ?: "",
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp,
                            color      = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}