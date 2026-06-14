package com.uas.myapplication.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.onboarding.components.DotsIndicator
import com.uas.myapplication.presentation.onboarding.components.OnboardingPageContent
import com.uas.myapplication.presentation.ui.theme.*
import kotlinx.coroutines.launch
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

data class OnboardingPage(
    val judul: String,
    val deskripsi: String,
    val tipeIlustrasi: IlustrasiType
)

enum class IlustrasiType { KACAPEMBESAR, TEMUKAN, SENYUM }

@Composable
fun OnboardingScreen(
    onSelesai: () -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    val onboardingPages = listOf(
        OnboardingPage(
            judul        = strings.onboardingTitle1,
            deskripsi    = strings.onboardingDesc1,
            tipeIlustrasi = IlustrasiType.KACAPEMBESAR
        ),
        OnboardingPage(
            judul        = strings.onboardingTitle2,
            deskripsi    = strings.onboardingDesc2,
            tipeIlustrasi = IlustrasiType.TEMUKAN
        ),
        OnboardingPage(
            judul        = strings.onboardingTitle3,
            deskripsi    = strings.onboardingDesc3,
            tipeIlustrasi = IlustrasiType.SENYUM
        )
    )

    val pagerState   = rememberPagerState(pageCount = { onboardingPages.size })
    val scope        = rememberCoroutineScope()
    val isLastPage   = pagerState.currentPage == onboardingPages.lastIndex

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalPager(
            state    = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            OnboardingPageContent(
                page      = onboardingPages[pageIndex],
                pageIndex = pageIndex
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DotsIndicator(
                totalDots    = onboardingPages.size,
                selectedDot  = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isLastPage) {
                        onSelesai()
                    } else {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text       = if (isLastPage) strings.btnAyoMulai else strings.btnLanjut,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (!isLastPage) {
                TextButton(onClick = onSelesai) {
                    Text(
                        text       = strings.btnLewati,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Onboarding Full Screen")
@Composable
fun PreviewOnboardingScreen() {
    MyApplicationTheme {
        OnboardingScreen(onSelesai = {})
    }
}