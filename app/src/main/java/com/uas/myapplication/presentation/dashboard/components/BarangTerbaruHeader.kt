package com.uas.myapplication.presentation.dashboard.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.Blue700
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun BarangTerbaruHeader(
    onLihatSemuaClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Barang Terbaru",
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Lihat Semua",
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            color = Blue700,
            modifier = Modifier.clickable {
                onLihatSemuaClick()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewBarangTerbaruHeader() {

    MyApplicationTheme {

        BarangTerbaruHeader(
            onLihatSemuaClick = {}
        )

    }
}
