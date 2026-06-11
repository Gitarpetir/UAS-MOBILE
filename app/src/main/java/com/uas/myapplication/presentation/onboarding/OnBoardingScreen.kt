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

data class OnboardingPage(
    val judul: String,
    val deskripsi: String,
    val tipeIlustrasi: IlustrasiType
)

enum class IlustrasiType { KACAPEMBESAR, TEMUKAN, SENYUM }

val onboardingPages = listOf(
    OnboardingPage(
        judul        = "Laporkan Barang Hilang",
        deskripsi    = "Laporkan barang yang hilang di kampus dengan cepat, beserta foto dan detailnya.",
        tipeIlustrasi = IlustrasiType.KACAPEMBESAR
    ),
    OnboardingPage(
        judul        = "Temukan Apa yang Hilang",
        deskripsi    = "Telusuri barang yang ditemukan dan klaim barang Anda yang hilang.",
        tipeIlustrasi = IlustrasiType.TEMUKAN
    ),
    OnboardingPage(
        judul        = "Bantu Orang Lain",
        deskripsi    = "Laporkan temuan barang dan bantu temukan pemiliknya",
        tipeIlustrasi = IlustrasiType.SENYUM
    )
)

// =============================================
// SCREEN UTAMA
// =============================================
@Composable
fun OnboardingScreen(
    onSelesai: () -> Unit
) {
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

        // Tombol & Indikator — di bagian bawah layar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Indikator titik halaman
            DotsIndicator(
                totalDots    = onboardingPages.size,
                selectedDot  = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tombol utama
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
                    text       = if (isLastPage) "Ayo Mulai" else "Lanjut",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }

            // Tombol Lewati — hanya tampil jika bukan halaman terakhir
            if (!isLastPage) {
                TextButton(onClick = onSelesai) {
                    Text(
                        text       = "Lewati",
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 14.sp,
                        color      = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                // Spacer agar layout tidak bergeser
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Onboarding Halaman 1")
@Composable
fun PreviewOnboarding1() {
    MyApplicationTheme {
        OnboardingPageContent(
            page      = onboardingPages[0],
            pageIndex = 0
        )
    }
}

@Preview(showBackground = true, name = "Onboarding Halaman 2")
@Composable
fun PreviewOnboarding2() {
    MyApplicationTheme {
        OnboardingPageContent(
            page      = onboardingPages[1],
            pageIndex = 1
        )
    }
}

@Preview(showBackground = true, name = "Onboarding Halaman 3")
@Composable
fun PreviewOnboarding3() {
    MyApplicationTheme {
        OnboardingPageContent(
            page      = onboardingPages[2],
            pageIndex = 2
        )
    }
}

@Preview(showBackground = true, name = "Onboarding Full Screen")
@Composable
fun PreviewOnboardingScreen() {
    MyApplicationTheme {
        OnboardingScreen(onSelesai = {})
    }
}