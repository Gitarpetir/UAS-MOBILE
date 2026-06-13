package com.uas.myapplication.presentation.detail.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.components.InfoItem
import com.uas.myapplication.presentation.ui.components.StatusBadge
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun DetailInfoSection(
    laporan: Laporan
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = laporan.namaBarang,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            StatusBadge(
                status = laporan.statusBarang
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = laporan.deskripsi,
            fontFamily = InterFontFamily,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "${strings.locationLabel} & ${strings.dateLabel}",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoItem(
                    icon = Icons.Default.LocationOn,
                    label = strings.locationLabel,
                    value = laporan.lokasi
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoItem(
                    icon = Icons.Default.CalendarToday,
                    label = strings.dateLabel,
                    value = laporan.tanggal
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "Detail Info Section"
)
@Composable
private fun PreviewDetailInfoSection() {

    MyApplicationTheme {

        DetailInfoSection(
            laporan = Laporan(
                id = "1",
                namaBarang = "Laptop Asus",
                deskripsi = "Laptop warna hitam ditemukan di Laboratorium Big Data.",
                lokasi = "Laboratorium Big Data",
                tanggal = "11 Juni 2026",
                statusBarang = StatusBarang.HILANG,

                fotoUrl = "",

                namaPelapor = "Alfi",
                nimPelapor = "2410817110009",

                whatsappPelapor = "",
                idPelapor = "",

                idPenemu = "",
                namaPenemu = "",
                nimPenemu = "",
                whatsappPenemu = ""
            )
        )
    }
}

