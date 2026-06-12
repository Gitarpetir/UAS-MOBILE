package com.uas.myapplication.presentation.auth.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.R
import com.uas.myapplication.presentation.ui.theme.InterFontFamily

@Composable
fun GoogleAuthButton(
    label: String,
    onClick: () -> Unit,
    isLoading: Boolean = false
) {
    OutlinedButton(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = "G",
                fontFamily = com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
