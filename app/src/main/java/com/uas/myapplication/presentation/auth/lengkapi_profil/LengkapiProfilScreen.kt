package com.uas.myapplication.presentation.auth.lengkapi_profil

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import androidx.compose.ui.text.font.FontWeight

// =============================================
// LENGKAPI PROFIL SCREEN
// =============================================
@Composable
fun LengkapiProfilScreen(
    viewModel      : LengkapiProfilViewModel,
    onSimpanSuccess: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)

    // Isi nama otomatis dari akun Google saat halaman pertama dibuka
    LaunchedEffect(Unit) {
        val namaGoogle = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
        viewModel.inisialisasiDariGoogle(namaGoogle)
    }

    // Navigasi ke Dashboard setelah profil berhasil disimpan
    LaunchedEffect(uiState.value.simpanSuccess) {
        if (uiState.value.simpanSuccess) {
            onSimpanSuccess()
            viewModel.resetSimpanSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // Judul
            Text(
                text       = strings.completeProfileTitle,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text       = strings.completeProfileSubtitle,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Field Nama Lengkap — terisi otomatis dari Google
            CariInTextField(
                value         = uiState.value.namaLengkap,
                onValueChange = viewModel::onNamaLengkapChange,
                label         = strings.fullNameLabel,
                placeholder   = strings.fullNamePlaceholder,
                leadingIcon   = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = CariInTheme.colors.textHint)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field NIM
            CariInTextField(
                value         = uiState.value.nim,
                onValueChange = viewModel::onNimChange,
                label         = strings.nimLabel,
                placeholder   = strings.nimPlaceholder,
                leadingIcon   = {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = CariInTheme.colors.textHint)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Field No. WhatsApp
            CariInTextField(
                value         = uiState.value.nomorWhatsapp,
                onValueChange = viewModel::onNomorWhatsappChange,
                label         = strings.waLabel,
                placeholder   = strings.waPlaceholder,
                leadingIcon   = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = CariInTheme.colors.textHint)
                }
            )

            // Pesan error
            AnimatedVisibility(
                visible = uiState.value.errorMessage != null,
                enter   = fadeIn(),
                exit    = fadeOut()
            ) {
                Text(
                    text       = uiState.value.errorMessage ?: "",
                    color      = MaterialTheme.colorScheme.error,
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    modifier   = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Tombol Simpan Profil
            Button(
                onClick  = viewModel::simpanProfil,
                enabled  = !uiState.value.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape  = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            ) {
                if (uiState.value.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.Person,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onPrimary,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = strings.btnSaveProfile,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 16.sp,
                        color      = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true, name = "Lengkapi Profil - Light", heightDp = 700)
@Composable
fun PreviewLengkapiProfil() {
    MyApplicationTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                "Lengkapi Profil",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
                color      = TextMain
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Lengkapi data profilmu di Cari.in",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSub
            )
            Spacer(modifier = Modifier.height(40.dp))
            // Nama sudah terisi otomatis dari Google
            CariInTextField(
                value         = "Azriel Gunawan",
                onValueChange = {},
                label         = "Nama Lengkap",
                placeholder   = "Azriel Gunawan",
                leadingIcon   = { Icon(Icons.Default.Person, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value         = "",
                onValueChange = {},
                label         = "NIM",
                placeholder   = "Nilai Induk Mahasiswa",
                leadingIcon   = { Icon(Icons.Default.Badge, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value         = "",
                onValueChange = {},
                label         = "No. WhatsApp",
                placeholder   = "Nomor Anda",
                leadingIcon   = { Icon(Icons.Default.Phone, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Simpan Profil",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Lengkapi Profil - Dark", heightDp = 700)
@Composable
fun PreviewLengkapiProfilDark() {
    MyApplicationTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Text(
                "Lengkapi Profil",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
                color      = TextMainDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Lengkapi data profilmu di Cari.in",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSubDark
            )
            Spacer(modifier = Modifier.height(40.dp))
            CariInTextField(
                value         = "Azriel Gunawan",
                onValueChange = {},
                label         = "Nama Lengkap",
                placeholder   = "Azriel Gunawan",
                leadingIcon   = { Icon(Icons.Default.Person, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value         = "",
                onValueChange = {},
                label         = "NIM",
                placeholder   = "Nilai Induk Mahasiswa",
                leadingIcon   = { Icon(Icons.Default.Badge, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value         = "",
                onValueChange = {},
                label         = "No. WhatsApp",
                placeholder   = "Nomor Anda",
                leadingIcon   = { Icon(Icons.Default.Phone, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Simpan Profil",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color      = Color.White
                )
            }
        }
    }
}