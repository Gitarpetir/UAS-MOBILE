package com.uas.myapplication.presentation.laporanku.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.TextSub


@Composable
fun JumlahLaporanText(
    jumlah: Int
) {
    Text(
        text = "$jumlah laporan",
        fontFamily = InterFontFamily,
        fontSize = 13.sp,
        color = TextSub,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
