package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun LabelWajib(
    teks: String
) {
    Row {

        Text(
            text = teks,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = " *",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = DangerRed
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLabelWajib() {
    MyApplicationTheme {
        LabelWajib("Nama Barang")
    }
}