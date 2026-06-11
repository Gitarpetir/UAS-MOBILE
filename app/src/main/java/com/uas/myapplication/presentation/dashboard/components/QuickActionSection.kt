package com.uas.myapplication.presentation.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.Blue700
import com.uas.myapplication.presentation.ui.theme.MyApplicationTheme
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun QuickActionSection(
    onLaporKehilanganClick: () -> Unit,
    onLaporTemuanClick    : () -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick  = onLaporKehilanganClick,
            modifier = Modifier.weight(1f).height(56.dp),
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text       = "Laporkan\nKehilangan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Color.White
            )
        }
        OutlinedButton(
            onClick  = onLaporTemuanClick,
            modifier = Modifier.weight(1f).height(56.dp),
            shape    = RoundedCornerShape(12.dp),
            border   = BorderStroke(1.5.dp, Blue700),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = Blue700)
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = Blue700, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text       = "Laporkan\nTemuan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 13.sp,
                color      = Blue700
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewQuickActionSection() {

    MyApplicationTheme {

        QuickActionSection(
            onLaporKehilanganClick = {},
            onLaporTemuanClick = {}
        )

    }
}