package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.DangerRed
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import com.uas.myapplication.presentation.ui.theme.SlateGray200
import com.uas.myapplication.presentation.ui.theme.TextSub
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun HapusDialog(
    namaBarang  : String,
    onKonfirmasi: () -> Unit,
    onBatal     : () -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    AlertDialog(
        onDismissRequest = onBatal,
        icon = {
            Icon(
                imageVector        = Icons.Default.DeleteForever,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.error,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = strings.deleteReportTitle,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = String.format(strings.deleteReportMessage, namaBarang),
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            Button(
                onClick = onKonfirmasi,
                colors  = ButtonDefaults.buttonColors(containerColor = DangerRed),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text(strings.btnDelete, fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onError)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onBatal,
                shape   = RoundedCornerShape(8.dp),
                border  = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(strings.btnCancel, fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true, name = "Dialog Hapus")
@Composable
fun PreviewHapusDialog() {
    MyApplicationTheme {
        HapusDialog(
            namaBarang   = "Jam Tangan Casio",
            onKonfirmasi = {},
            onBatal      = {}
        )
    }
}