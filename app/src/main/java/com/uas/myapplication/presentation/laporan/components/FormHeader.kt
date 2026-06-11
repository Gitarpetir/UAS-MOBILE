package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun FormHeader(
    title: String
) {
    Column {

        Text(
            text = title,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(
            modifier = androidx.compose.ui.Modifier.height(20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFormHeader() {
    MyApplicationTheme {
        FormHeader(
            title = "Laporkan Barang"
        )
    }
}