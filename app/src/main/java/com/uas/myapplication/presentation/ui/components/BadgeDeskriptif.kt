package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.laporanku.TabLaporanku
import com.uas.myapplication.presentation.ui.theme.DangerRed
import com.uas.myapplication.presentation.ui.theme.DangerRedLight
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.theme.NeutralGray
import com.uas.myapplication.presentation.ui.theme.NeutralGrayLight
import com.uas.myapplication.presentation.ui.theme.SuccessGreen
import com.uas.myapplication.presentation.ui.theme.SuccessGreenLight

@Composable
fun BadgeDeskriptif(
    laporan : Laporan,
    tabAktif: TabLaporanku
) {
    val (label, bgColor, textColor) = when {
        // Tab Barangmu
        tabAktif == TabLaporanku.BARANGMU &&
                laporan.statusBarang == StatusBarang.HILANG ->
            Triple("Barangmu Sedang Dicari", DangerRedLight, DangerRed)

        tabAktif == TabLaporanku.BARANGMU &&
                laporan.statusBarang == StatusBarang.DITEMUKAN ->
            Triple("Barangmu Ditemukan", SuccessGreenLight, SuccessGreen)

        // Tab Temuanmu
        tabAktif == TabLaporanku.TEMUANMU &&
                laporan.statusBarang == StatusBarang.DITEMUKAN ->
            Triple("Ditemukan", SuccessGreenLight, SuccessGreen)

        tabAktif == TabLaporanku.TEMUANMU &&
                laporan.statusBarang == StatusBarang.SELESAI ->
            Triple("Selesai", NeutralGrayLight, NeutralGray)

        // Fallback
        else -> Triple("Tidak Diketahui", NeutralGrayLight, NeutralGray)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            color = textColor
        )
    }
}

@Preview(showBackground = true, name = "Badge Deskriptif - Semua")
@Composable
fun PreviewBadgeDeskriptif() {
    MyApplicationTheme {
        Column(
            modifier              = Modifier.padding(16.dp),
            verticalArrangement   = Arrangement.spacedBy(8.dp)
        ) {
            BadgeDeskriptif(
                laporan = Laporan(statusBarang = StatusBarang.HILANG),
                tabAktif = TabLaporanku.BARANGMU
            )
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.DITEMUKAN), tabAktif = TabLaporanku.BARANGMU)
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.DITEMUKAN), tabAktif = TabLaporanku.TEMUANMU)
            BadgeDeskriptif(laporan = Laporan(statusBarang = StatusBarang.SELESAI),   tabAktif = TabLaporanku.TEMUANMU)
        }
    }
}