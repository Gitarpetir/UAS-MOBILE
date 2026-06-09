package com.uas.myapplication.presentation.ui.components

import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun CariInFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontFamily = InterFontFamily,
                fontWeight = if (isSelected)
                    FontWeight.SemiBold
                else
                    FontWeight.Normal,
                fontSize = 13.sp
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Blue700,
            selectedLabelColor = Color.White,
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
            labelColor = TextSub
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            selectedBorderColor = Blue700,
            selectedBorderWidth = 0.dp,
            borderColor = SlateGray200,
            borderWidth = 1.dp
        )
    )
}