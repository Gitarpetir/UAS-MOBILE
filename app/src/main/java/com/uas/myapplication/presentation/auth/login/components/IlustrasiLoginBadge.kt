package com.uas.myapplication.presentation.auth.login.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.onboarding.components.IlustrasiTemukan

@Composable
fun IlustrasiLoginBadge() {
    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(160.dp)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        )
        Box(modifier = Modifier.size(70.dp)) {
            IlustrasiTemukan()
        }
    }
}
