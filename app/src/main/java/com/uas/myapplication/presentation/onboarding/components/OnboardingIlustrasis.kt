package com.uas.myapplication.presentation.onboarding.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.ui.theme.SuccessGreenLight

@Composable
fun IlustrasiKacaPembesar() {
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
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
        )

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

    scope.drawCircle(
        color  = Color.White,
        radius = radiusLensa,
        center = Offset(cx - scope.size.width * 0.08f, cy - scope.size.height * 0.08f)
    )

    scope.drawCircle(
        color  = Color(0xFF0284C7),
        radius = radiusLensa,
        center = Offset(cx - scope.size.width * 0.08f, cy - scope.size.height * 0.08f),
        style  = Stroke(width = tebal)
    )

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
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(SuccessGreenLight, CircleShape)
        )

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

    scope.drawRoundRect(
        color       = Color(0xFF10B981),
        topLeft     = Offset(padding, padding),
        size        = Size(scope.size.width - padding * 2, scope.size.height - padding * 2),
        cornerRadius = corner
    )

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
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(Color(0xFFFEF9C3), CircleShape)
        )

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

    scope.drawCircle(
        color  = Color(0xFFF59E0B),
        radius = r,
        center = Offset(cx, cy)
    )

    scope.drawCircle(
        color  = Color(0xFF1C1917),
        radius = r * 0.09f,
        center = Offset(cx - r * 0.3f, cy - r * 0.15f)
    )

    scope.drawCircle(
        color  = Color(0xFF1C1917),
        radius = r * 0.09f,
        center = Offset(cx + r * 0.3f, cy - r * 0.15f)
    )

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
