package com.uas.myapplication.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.dashboard.DashboardUiState
import com.uas.myapplication.presentation.ui.components.StatistikCard
import com.uas.myapplication.presentation.ui.theme.Blue700
import com.uas.myapplication.presentation.ui.theme.Blue800
import com.uas.myapplication.presentation.ui.theme.DangerRed
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import com.uas.myapplication.presentation.ui.theme.SuccessGreen
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun DashboardHeader (uiState: DashboardUiState) {
    val strings = StringProvider.get(LocalBahasa.current)
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val backgroundColor = if (isDark) com.uas.myapplication.presentation.ui.theme.DarkBackground else MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Column {
            Text(
                text       = "Cari.in",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah   = uiState.jumlahHilang,
                    label    = strings.lostReportsHeader,
                    warna    = DangerRed
                )
                StatistikCard(
                    modifier = Modifier.weight(1f),
                    jumlah   = uiState.jumlahDitemukan,
                    label    = strings.foundReportsHeader,
                    warna    = SuccessGreen
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDashboardHeader() {

    MyApplicationTheme {

        DashboardHeader(
            uiState = DashboardUiState(
                jumlahHilang = 12,
                jumlahDitemukan = 7
            )
        )

    }
}
