package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun StatusBadge(status: StatusBarang) {
    val (label, bgColor, textColor) = when (status) {
        StatusBarang.HILANG    -> Triple("Hilang",    DangerRedLight,    DangerRed)
        StatusBarang.DITEMUKAN -> Triple("Ditemukan", SuccessGreenLight, SuccessGreen)
        StatusBarang.SELESAI   -> Triple("Selesai",   NeutralGrayLight,  NeutralGray)
    }

    Box(
        modifier         = Modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize   = 11.sp,
            color      = textColor
        )
    }
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Status Badge - Semua")
@Composable
fun PreviewStatusBadge() {
    MyApplicationTheme {
        Row(
            modifier              = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatusBadge(status = StatusBarang.HILANG)
            StatusBadge(status = StatusBarang.DITEMUKAN)
            StatusBadge(status = StatusBarang.SELESAI)
        }
    }
}