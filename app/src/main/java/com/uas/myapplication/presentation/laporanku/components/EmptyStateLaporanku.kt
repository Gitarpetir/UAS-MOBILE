package com.uas.myapplication.presentation.laporanku.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.uas.myapplication.presentation.laporanku.TabLaporanku
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun EmptyStateLaporanku(
    tabAktif: TabLaporanku
) {

    val judulKosong = when (tabAktif) {
        TabLaporanku.BARANGKU -> "Belum ada laporan kehilangan"
        TabLaporanku.TEMUANKU -> "Belum ada laporan temuan"
        TabLaporanku.KONTRIBUSI -> "Belum ada kontribusi"
    }

    val deskripsiKosong = when (tabAktif) {
        TabLaporanku.BARANGKU -> "Tekan tombol + Baru untuk membuat laporan kehilangan"
        TabLaporanku.TEMUANKU -> "Tekan tombol + Baru untuk membuat laporan temuan"
        TabLaporanku.KONTRIBUSI -> "Kontribusimu akan muncul di sini"
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = null,
            tint = TextHint,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = judulKosong,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = TextSub
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = deskripsiKosong,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            color = TextHint
        )
    }
}