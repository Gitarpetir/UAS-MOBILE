package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun TeksError(
    pesan: String
) {
    Text(
        text = pesan,
        color = DangerRed,
        fontFamily = InterFontFamily,
        fontSize = 12.sp,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewTeksError() {
    MyApplicationTheme {
        TeksError(
            "Nama barang wajib diisi"
        )
    }
}