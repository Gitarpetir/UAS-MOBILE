package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.laporanku.TabLaporanku
import com.uas.myapplication.presentation.ui.theme.DangerRed
import com.uas.myapplication.presentation.ui.theme.DangerRedLight
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import com.uas.myapplication.presentation.ui.theme.SlateGray100
import com.uas.myapplication.presentation.ui.theme.SlateGray200
import com.uas.myapplication.presentation.ui.theme.TextSub
import kotlin.text.ifEmpty
import com.uas.myapplication.presentation.ui.theme.SuccessGreen
import com.uas.myapplication.presentation.ui.theme.SuccessGreenLight
// =============================================
// CARD LAPORANKU — dengan badge deskriptif
// =============================================
@Composable
fun LaporankuCard(
    laporan     : Laporan,
    tabAktif    : TabLaporanku,
    bolehEditHapus : Boolean,
    onEditClick : () -> Unit,
    onHapusClick: () -> Unit
) {
    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape     = RoundedCornerShape(12.dp),
        colors    = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Baris atas — foto + info
            Row(verticalAlignment = Alignment.Top) {
                // Foto barang
                AsyncImage(
                    model              = laporan.fotoUrl.ifEmpty { null },
                    contentDescription = "Foto ${laporan.namaBarang}",
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SlateGray100)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Nama barang
                    Text(
                        text       = laporan.namaBarang,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    if (
                        tabAktif == TabLaporanku.BARANGKU &&
                        laporan.statusBarang == StatusBarang.DITEMUKAN
                    ) {

                        Box(
                            modifier = Modifier
                                .background(
                                    SuccessGreenLight,
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Barangmu Ditemukan",
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                                color = SuccessGreen
                            )
                        }

                    } else {

                        StatusBadge(
                            status = laporan.statusBarang
                        )

                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Spacer(modifier = Modifier.height(6.dp))

                    // Lokasi
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = TextSub, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = laporan.lokasi, fontFamily = InterFontFamily, fontSize = 12.sp, color = TextSub)
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Tanggal
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, null, tint = TextSub, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(text = laporan.tanggal, fontFamily = InterFontFamily, fontSize = 12.sp, color = TextSub)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Divider
            HorizontalDivider(color = SlateGray100)

            Spacer(modifier = Modifier.height(10.dp))

            // Baris bawah — tombol Edit & Hapus
            if (bolehEditHapus) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Tombol Edit
                    OutlinedButton(
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateGray200)
                    ) {
                        Icon(
                            Icons.Default.Edit,
                            null,
                            tint = TextSub,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Edit",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = TextSub
                        )
                    }

                    // Tombol Hapus
                    Button(
                        onClick = onHapusClick,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DangerRedLight,
                            contentColor = DangerRed
                        ),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            null,
                            tint = DangerRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Hapus",
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp,
                            color = DangerRed
                        )
                    }
                }
            }
        }
    }
}
