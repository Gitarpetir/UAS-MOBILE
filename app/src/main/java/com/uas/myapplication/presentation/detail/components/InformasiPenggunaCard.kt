package com.uas.myapplication.presentation.detail.components

import com.uas.myapplication.presentation.ui.components.common.InfoItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun InformasiPenggunaCard(
    title: String,
    nama: String,
    nim: String
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(
                icon = Icons.Default.Person,
                label = strings.nameLabel,
                value = nama
            )

            Spacer(modifier = Modifier.height(10.dp))

            InfoItem(
                icon = Icons.Default.Badge,
                label = strings.nimLabel,
                value = nim
            )

        }
    }
}

@Preview(
    showBackground = true,
    name = "Informasi Pelapor"
)
@Composable
private fun PreviewInformasiPelaporCard() {

    MyApplicationTheme {

        InformasiPenggunaCard(
            title = "Informasi Pelapor",
            nama = "Muhammad Alfi Gunawan",
            nim = "2410817110009"
        )

    }
}

@Preview(
    showBackground = true,
    name = "Informasi Penemu"
)
@Composable
private fun PreviewInformasiPenemuCard() {

    MyApplicationTheme {

        InformasiPenggunaCard(
            title = "Informasi Penemu",
            nama = "Budi Santoso",
            nim = "2410817110010"
        )

    }
}