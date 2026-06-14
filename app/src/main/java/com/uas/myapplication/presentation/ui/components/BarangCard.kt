package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme

@Composable
fun BarangCard(
    laporan: Laporan,
    onClick: () -> Unit,
    onDeleteClick: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier          = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto barang
            AsyncImage(
                model              = laporan.fotoUrl.ifEmpty { null },
                contentDescription = "Foto ${laporan.namaBarang}",
                contentScale       = ContentScale.Crop,
                modifier           = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CariInTheme.colors.imagePlaceholder)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info barang
            Column(modifier = Modifier.weight(1f)) {
                // Nama barang
                Text(
                    text       = laporan.namaBarang,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = MaterialTheme.colorScheme.onSurface,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Lokasi
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier           = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text       = laporan.lokasi,
                        fontFamily = InterFontFamily,
                        fontSize   = 12.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines   = 1,
                        overflow   = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Tanggal
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier           = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text       = laporan.tanggal,
                        fontFamily = InterFontFamily,
                        fontSize   = 12.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Badge status
            StatusBadge(status = laporan.statusBarang)

            if (onDeleteClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(DangerRedLight, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Laporan",
                        tint = DangerRed,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Barang Card - Hilang")
@Composable
fun PreviewBarangCardHilang() {
    MyApplicationTheme {
        BarangCard(
            laporan = Laporan(
                id           = "1",
                namaBarang   = "Jam Tangan Casio",
                lokasi       = "Ruang Baca Lt.2",
                tanggal      = "12 April 2026",
                statusBarang = StatusBarang.HILANG
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Barang Card - Ditemukan")
@Composable
fun PreviewBarangCardDitemukan() {
    MyApplicationTheme {
        BarangCard(
            laporan = Laporan(
                id           = "2",
                namaBarang   = "Kunci Motor Honda",
                lokasi       = "Kantin Teknik",
                tanggal      = "11 April 2026",
                statusBarang = StatusBarang.DITEMUKAN
            ),
            onClick = {}
        )
    }
}