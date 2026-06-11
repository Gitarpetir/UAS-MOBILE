package com.uas.myapplication.presentation.profil.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun BahasaDialog(
    bahasaAktif: String,
    onPilih    : (String) -> Unit,
    onBatal    : () -> Unit
) {
    AlertDialog(
        onDismissRequest = onBatal,
        icon = {
            Icon(
                imageVector        = Icons.Default.Language,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text       = "Pilih Bahasa",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                BahasaItem(
                    label    = "Indonesia",
                    isAktif  = bahasaAktif == "id",
                    onClick  = { onPilih("id") }
                )
                BahasaItem(
                    label    = "English",
                    isAktif  = bahasaAktif == "en",
                    onClick  = { onPilih("en") }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onBatal) {
                Text(
                    text       = "Batal",
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Medium,
                    color      = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape          = RoundedCornerShape(16.dp)
    )
}

@Composable
fun BahasaItem(
    label  : String,
    isAktif: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isAktif) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text       = label,
            fontFamily = InterFontFamily,
            fontWeight = if (isAktif) FontWeight.SemiBold else FontWeight.Normal,
            fontSize   = 15.sp,
            color      = if (isAktif) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        if (isAktif) {
            Icon(
                imageVector        = Icons.Default.Check,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary,
                modifier           = Modifier.size(18.dp)
            )
        }
    }
}
