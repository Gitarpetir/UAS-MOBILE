package com.uas.myapplication.presentation.laporanku.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.Blue700
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily
import com.uas.myapplication.presentation.ui.theme.TextMain

@Composable
fun LaporankuHeader(
    onTambahClick: () -> Unit
){
    Row(
    modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 20.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment     = Alignment.CenterVertically
) {
    Text("Laporanku", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = TextMain)
        Button(
            onClick = onTambahClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue700
            ),
            contentPadding = PaddingValues(
                horizontal = 14.dp,
                vertical = 8.dp
            )
        ) {
        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text("Baru", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = Color.White)
    }
}}
