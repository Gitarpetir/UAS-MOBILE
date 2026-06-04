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

@Composable
fun HapusDialog(
    namaBarang  : String,
    onKonfirmasi: () -> Unit,
    onBatal     : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onBatal,
        icon = {
            Icon(
                imageVector        = Icons.Default.DeleteForever,
                contentDescription = null,
                tint               = DangerRed,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Hapus Laporan?",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = "Laporan \"$namaBarang\" akan dihapus secara permanen dan tidak bisa dikembalikan.",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSub
            )
        },
        confirmButton = {
            Button(
                onClick = onKonfirmasi,
                colors  = ButtonDefaults.buttonColors(containerColor = DangerRed),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text("Hapus", fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onBatal,
                shape   = RoundedCornerShape(8.dp),
                border  = androidx.compose.foundation.BorderStroke(1.dp, SlateGray200)
            ) {
                Text("Batal", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, color = TextSub)
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