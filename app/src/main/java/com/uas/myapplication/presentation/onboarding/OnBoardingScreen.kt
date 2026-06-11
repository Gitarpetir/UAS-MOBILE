package com.uas.myapplication.presentation.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
// KONTEN TIAP HALAMAN
// =============================================
@Composable
fun OnboardingPageContent(
    page: OnboardingPage,
    pageIndex: Int
) {
    // Animasi fade-in saat halaman pertama kali muncul
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
        // Ilustrasi
        when (page.tipeIlustrasi) {
            IlustrasiType.KACAPEMBESAR  -> IlustrasiKacaPembesar()
            IlustrasiType.TEMUKAN -> IlustrasiTemukan()
            IlustrasiType.SENYUM  -> IlustrasiSenyum()
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Judul
        Text(
            text       = page.judul,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 22.sp,
            color      = MaterialTheme.colorScheme.onBackground,
            textAlign  = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Deskripsi
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

// Ganti fungsi IlustrasiKompas() dan gambarKompas() dengan kode di bawah ini

@Composable
fun IlustrasiKacaPembesar() {
    // Animasi goyang kiri-kanan seperti sedang mencari
    val rotasi by rememberInfiniteTransition(label = "kaca").animateFloat(
        initialValue  = -15f,
        targetValue   = 15f,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotasiKaca"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(200.dp)
    ) {
        // Lingkaran latar biru muda
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        )

        // Canvas ikon kaca pembesar
        Canvas(
            modifier = Modifier
                .size(90.dp)
                .rotate(rotasi)
        ) {
            gambarKacaPembesar(this)
        }
    }
}

fun gambarKacaPembesar(scope: DrawScope) {
    val cx         = scope.size.width / 2
    val cy         = scope.size.height / 2
    val radiusLensa = scope.size.width * 0.32f
    val tebal      = scope.size.width * 0.09f

    // Lingkaran lensa (putih isi)
    scope.drawCircle(
        color  = Color.White,
        radius = radiusLensa,
        center = Offset(cx - scope.size.width * 0.08f, cy - scope.size.height * 0.08f)
    )

    // Cincin lensa (biru outline)
    scope.drawCircle(
        color  = Color(0xFF0284C7),
        radius = radiusLensa,
        center = Offset(cx - scope.size.width * 0.08f, cy - scope.size.height * 0.08f),
        style  = Stroke(width = tebal)
    )

    // Gagang kaca pembesar (biru, sudut kanan bawah)
    val gagangStart = Offset(
        x = cx - scope.size.width * 0.08f + radiusLensa * 0.68f,
        y = cy - scope.size.height * 0.08f + radiusLensa * 0.68f
    )
    val gagangEnd = Offset(
        x = cx + scope.size.width * 0.32f,
        y = cy + scope.size.height * 0.32f
    )
    scope.drawLine(
        color       = Color(0xFF0284C7),
        start       = gagangStart,
        end         = gagangEnd,
        strokeWidth = tebal * 1.1f,
        cap         = StrokeCap.Round
    )
}

// =============================================
// ILUSTRASI 2 — KOTAK CENTANG (POP SCALE)
// =============================================
@Composable
fun IlustrasiTemukan() {
    var mulai by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue   = if (mulai) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMedium
        ),
        label = "scaleTemukan"
    )

    LaunchedEffect(Unit) { mulai = true }

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(200.dp)
    ) {
        // Lingkaran latar hijau muda
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(SuccessGreenLight, CircleShape)
        )

        // Kotak rounded dengan centang
        Canvas(
            modifier = Modifier
                .size(80.dp)
                .scale(scale)
        ) {
            gambarKotakCentang(this)
        }
    }
}

fun gambarKotakCentang(scope: DrawScope) {
    val padding = scope.size.width * 0.05f
    val corner  = CornerRadius(scope.size.width * 0.25f)

    // Kotak hijau rounded
    scope.drawRoundRect(
        color       = Color(0xFF10B981),
        topLeft     = Offset(padding, padding),
        size        = Size(scope.size.width - padding * 2, scope.size.height - padding * 2),
        cornerRadius = corner
    )

    // Centang putih
    val cx = scope.size.width / 2
    val cy = scope.size.height / 2
    val path = Path().apply {
        moveTo(cx - scope.size.width * 0.22f, cy)
        lineTo(cx - scope.size.width * 0.05f, cy + scope.size.height * 0.18f)
        lineTo(cx + scope.size.width * 0.25f, cy - scope.size.height * 0.15f)
    }
    scope.drawPath(
        path   = path,
        color  = Color.White,
        style  = Stroke(
            width = scope.size.width * 0.1f,
            cap   = StrokeCap.Round
        )
    )
}

// =============================================
// ILUSTRASI 3 — WAJAH SENYUM (BOUNCE)
// =============================================
@Composable
fun IlustrasiSenyum() {
    var mulai by remember { mutableStateOf(false) }
    val offsetY by animateFloatAsState(
        targetValue   = if (mulai) 0f else 60f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessMediumLow
        ),
        label = "bounceSenyum"
    )
    val alpha by animateFloatAsState(
        targetValue   = if (mulai) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label         = "alphaSenyum"
    )

    LaunchedEffect(Unit) { mulai = true }

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(200.dp)
    ) {
        // Lingkaran latar kuning muda
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(Color(0xFFFEF9C3), CircleShape)
        )

        // Wajah senyum
        Canvas(
            modifier = Modifier
                .size(90.dp)
                .offset(y = offsetY.dp)
                .alpha(alpha)
        ) {
            gambarWajahSenyum(this)
        }
    }
}

fun gambarWajahSenyum(scope: DrawScope) {
    val cx = scope.size.width / 2
    val cy = scope.size.height / 2
    val r  = scope.size.width / 2

    // Badan wajah — oranye/kuning
    scope.drawCircle(
        color  = Color(0xFFF59E0B),
        radius = r,
        center = Offset(cx, cy)
    )

    // Mata kiri
    scope.drawCircle(
        color  = Color(0xFF1C1917),
        radius = r * 0.09f,
        center = Offset(cx - r * 0.3f, cy - r * 0.15f)
    )

    // Mata kanan
    scope.drawCircle(
        color  = Color(0xFF1C1917),
        radius = r * 0.09f,
        center = Offset(cx + r * 0.3f, cy - r * 0.15f)
    )

    // Senyum — arc bawah
    val senyumPath = Path().apply {
        moveTo(cx - r * 0.35f, cy + r * 0.1f)
        quadraticTo(
            cx, cy + r * 0.5f,
            cx + r * 0.35f, cy + r * 0.1f
        )
    }
    scope.drawPath(
        path  = senyumPath,
        color = Color(0xFF1C1917),
        style = Stroke(
            width = r * 0.1f,
            cap   = StrokeCap.Round
        )
    )
}

// =============================================
// KOMPONEN INDIKATOR TITIK
// =============================================
@Composable
fun DotsIndicator(
    totalDots:   Int,
    selectedDot: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            val isSelected = index == selectedDot
            val lebarDot by animateDpAsState(
                targetValue   = if (isSelected) 24.dp else 8.dp,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label         = "lebarDot"
            )
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(lebarDot)
                    .background(
                        color  = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        shape  = CircleShape
                    )
            )
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