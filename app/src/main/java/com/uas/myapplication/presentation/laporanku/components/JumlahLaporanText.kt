package com.uas.myapplication.presentation.laporanku.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.TextSub
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun JumlahLaporanText(
    jumlah: Int
) {
    val strings = StringProvider.get(LocalBahasa.current)
    Text(
        text = "$jumlah ${strings.reportsCountLabel}",
        fontFamily = InterFontFamily,
        fontSize = 13.sp,
        color = TextSub,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}
