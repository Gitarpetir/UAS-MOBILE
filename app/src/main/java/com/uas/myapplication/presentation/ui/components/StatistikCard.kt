package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun StatistikCard(
    modifier: Modifier = Modifier,
    jumlah  : Int,
    label   : String,
    warna   : Color
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        colors   = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.15f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier         = Modifier
                    .size(32.dp)
                    .background(warna.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(14.dp).background(warna, CircleShape))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text       = jumlah.toString(),
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
                color      = Color.White
            )
            Text(
                text       = label,
                fontFamily = InterFontFamily,
                fontSize   = 12.sp,
                color      = Color.White.copy(alpha = 0.85f)
            )
        }
    }
}
