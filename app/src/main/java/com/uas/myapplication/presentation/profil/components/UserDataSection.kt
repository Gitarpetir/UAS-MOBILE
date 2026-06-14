package com.uas.myapplication.presentation.profil.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun UserDataSection(
    user: User?,
    sectionTitle: String,
    emailLabel: String,
    nimLabel: String,
    waLabel: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text       = sectionTitle,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 16.sp,
            color      = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        DataCard(
            icon  = Icons.Default.Email,
            label = emailLabel,
            value = user?.email ?: "..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        DataCard(
            icon  = Icons.Default.Badge,
            label = nimLabel,
            value = user?.nim ?: "..."
        )

        Spacer(modifier = Modifier.height(8.dp))

        DataCard(
            icon = Icons.Default.Phone,
            label = waLabel,
            value = user?.nomorWhatsapp ?: "-"
        )
    }
}
