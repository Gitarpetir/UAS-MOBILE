package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun TanggalField(
    tanggal: String,
    isError: Boolean,
    onClick: () -> Unit
) {

    LabelWajib("Tanggal")

    Spacer(modifier = Modifier.height(6.dp))

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {

        OutlinedTextField(
            value = tanggal,
            onValueChange = {},
            placeholder = {
                Text(
                    "Pilih tanggal",
                    fontFamily = InterFontFamily,
                    color = TextHint
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                    tint = TextHint
                )
            },
            readOnly = true,
            isError = isError,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue700,
                unfocusedBorderColor = SlateGray200,
                errorBorderColor = DangerRed
            )
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    onClick()
                }
        )
    }

    if (isError) {
        TeksError("Tanggal wajib dipilih")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTanggalKosong() {
    MyApplicationTheme {
        TanggalField(
            tanggal = "",
            isError = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTanggalIsi() {
    MyApplicationTheme {
        TanggalField(
            tanggal = "11 Juni 2026",
            isError = false,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTanggalError() {
    MyApplicationTheme {
        TanggalField(
            tanggal = "",
            isError = true,
            onClick = {}
        )
    }
}