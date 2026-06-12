package com.uas.myapplication.presentation.admin.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.admin.dashboard.DashboardAdminUiState
import com.uas.myapplication.presentation.ui.components.StatistikCard
import com.uas.myapplication.presentation.ui.theme.DangerRed
import com.uas.myapplication.presentation.ui.theme.NeutralGrayLight
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import com.uas.myapplication.presentation.ui.theme.SuccessGreen

@Composable
fun DashboardAdminHeader(
    uiState: DashboardAdminUiState
) {
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val backgroundColor = if (isDark) com.uas.myapplication.presentation.ui.theme.DarkBackground else MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(
                horizontal = 16.dp,
                vertical = 24.dp
            )
    ) {
        Column {
            Text(
                text = "Cari.in Admin",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah = uiState.jumlahHilang,
                    label = strings.statLostItems,
                    warna = DangerRed
                )

                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah = uiState.jumlahDitemukan,
                    label = strings.statFoundItems,
                    warna = SuccessGreen
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            StatistikCard(
                modifier = Modifier.fillMaxWidth(),
                jumlah = uiState.jumlahSelesai,
                label = strings.statCompletedReports,
                warna = NeutralGrayLight
            )
        }
    }
}
