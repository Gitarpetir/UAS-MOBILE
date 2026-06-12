package com.uas.myapplication.presentation.detail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun DetailActionSection(
    laporan: Laporan,
    isAdmin: Boolean,
    isLoading: Boolean,
    onAksiClick: () -> Unit,
    onSelesaikan: () -> Unit,
    onKembali: () -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    if (laporan.statusBarang != StatusBarang.SELESAI) {

        Button(
            onClick = onAksiClick,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.5.dp
                )

            } else {

                Text(
                    text = when (laporan.statusBarang) {
                        StatusBarang.HILANG -> strings.btnFoundThisItem
                        StatusBarang.DITEMUKAN -> strings.btnThisIsMine
                        else -> ""
                    },
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    if (isAdmin) {

        Button(
            onClick = onSelesaikan,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SuccessGreen
            )
        ) {

            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = strings.btnFinishReport,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    OutlinedButton(
        onClick = onKembali,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        )
    ) {

        Text(
            text = strings.btnBack,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Preview(
    showBackground = true,
    name = "Action Section - Hilang"
)
@Composable
private fun PreviewDetailActionSectionHilang() {

    MyApplicationTheme {

        DetailActionSection(
            laporan = Laporan(
                id = "1",
                namaBarang = "Laptop Asus",
                deskripsi = "Laptop",
                lokasi = "Lab",
                tanggal = "11 Juni 2026",
                statusBarang = StatusBarang.HILANG,

                fotoUrl = "",

                namaPelapor = "",
                nimPelapor = "",
                whatsappPelapor = "",
                idPelapor = "",

                idPenemu = "",
                namaPenemu = "",
                nimPenemu = "",
                whatsappPenemu = ""
            ),
            isAdmin = false,
            isLoading = false,
            onAksiClick = {},
            onSelesaikan = {},
            onKembali = {}
        )
    }
}

@Preview(
    showBackground = true,
    name = "Action Section - Admin"
)
@Composable
private fun PreviewDetailActionSectionAdmin() {

    MyApplicationTheme {

        DetailActionSection(
            laporan = Laporan(
                id = "1",
                namaBarang = "Laptop Asus",
                deskripsi = "Laptop",
                lokasi = "Lab",
                tanggal = "11 Juni 2026",
                statusBarang = StatusBarang.DITEMUKAN,

                fotoUrl = "",

                namaPelapor = "",
                nimPelapor = "",
                whatsappPelapor = "",
                idPelapor = "",

                idPenemu = "",
                namaPenemu = "",
                nimPenemu = "",
                whatsappPenemu = ""
            ),
            isAdmin = true,
            isLoading = false,
            onAksiClick = {},
            onSelesaikan = {},
            onKembali = {}
        )
    }
}