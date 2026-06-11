package com.uas.myapplication.presentation.onboarding.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.onboarding.IlustrasiType
import com.uas.myapplication.presentation.onboarding.OnboardingPage
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue    = if (visible) 1f else 0f,
        animationSpec  = tween(durationMillis = 600),
        label          = "fadeIn"
    )

    LaunchedEffect(pageIndex) { visible = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 80.dp, bottom = 160.dp)
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (page.tipeIlustrasi) {
            IlustrasiType.KACAPEMBESAR  -> IlustrasiKacaPembesar()
            IlustrasiType.TEMUKAN -> IlustrasiTemukan()
            IlustrasiType.SENYUM  -> IlustrasiSenyum()
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text       = page.judul,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 22.sp,
            color      = MaterialTheme.colorScheme.onBackground,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text       = page.deskripsi,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize   = 14.sp,
            color      = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign  = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}
