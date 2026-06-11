package com.uas.myapplication.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
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
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun DialogKonfirmasiTemukan(
    onKonfirmasi: () -> Unit,
    onBatal     : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onBatal,
        icon = {
            Icon(
                imageVector        = Icons.Default.Search,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Konfirmasi Penemuan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = "Apakah Anda benar menemukan barang ini? Jika ya, mohon segera serahkan barang tersebut ke Ruangan Bersama.",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onKonfirmasi,
                colors  = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text       = "Ya, Saya Temukan",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onBatal,
                shape   = RoundedCornerShape(8.dp),
                border  = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text       = "Batal",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

@Composable
fun DialogKonfirmasiMilik(
    onMengerti: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onMengerti,
        icon = {
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = SuccessGreen,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Ambil Barangmu",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Text(
                text       = "Silakan menuju Ruang Bersama untuk mengambil barang Anda dengan menunjukkan identitas.",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onMengerti,
                colors  = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape   = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text       = "Mengerti",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

@Preview(
    showBackground = true,
    name = "Dialog Konfirmasi Temukan"
)
@Composable
private fun PreviewDialogTemukan() {
    MyApplicationTheme {
        DialogKonfirmasiTemukan(
            onKonfirmasi = {},
            onBatal = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Dialog Konfirmasi Milik"
)
@Composable
private fun PreviewDialogMilik() {
    MyApplicationTheme {
        DialogKonfirmasiMilik(
            onMengerti = {}
        )
    }
}