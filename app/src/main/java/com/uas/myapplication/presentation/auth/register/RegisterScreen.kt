package com.uas.myapplication.presentation.auth.register

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.uas.myapplication.R
import com.uas.myapplication.presentation.auth.components.AuthDivider
import com.uas.myapplication.presentation.auth.components.AuthHeader
import com.uas.myapplication.presentation.auth.components.GoogleAuthButton
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme

@Composable
fun RegisterScreen(
    viewModel        : RegisterViewModel,
    onRegisterSuccess: () -> Unit,
    onGoogleSuccess  : () -> Unit,
    onSudahPunyaAkun : () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context  = LocalContext.current
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { token -> viewModel.loginWithGoogle(token) }
            } catch (e: ApiException) { }
        }
    }

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            onRegisterSuccess()
            viewModel.resetRegisterSuccess()
        }
    }

    LaunchedEffect(uiState.googleSuccess) {
        if (uiState.googleSuccess) {
            onGoogleSuccess()
            viewModel.resetRegisterSuccess()
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
            Spacer(modifier = Modifier.height(52.dp))

            AuthHeader(
                title = strings.registerTitle,
                subtitle = strings.registerSubtitle
            )
            
            Spacer(modifier = Modifier.height(32.dp))

            // Form Register
            com.uas.myapplication.presentation.auth.register.components.RegisterForm(
                uiState = uiState,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(20.dp))

            AuthDivider(label = strings.orText)

            // Tombol Daftar dengan Google
            GoogleAuthButton(
                label = strings.btnGoogleConnect,
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleLauncher.launch(googleSignInClient.signInIntent)
                },
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Teks "Sudah punya akun? Masuk"
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = onSudahPunyaAkun) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(
                                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = InterFontFamily,
                                fontSize   = 14.sp
                            )) { append(strings.alreadyHaveAccountText) }
                            withStyle(SpanStyle(
                                color      = MaterialTheme.colorScheme.primary,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 14.sp
                            )) { append(strings.btnLogin) }
                        }
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
@Preview(showBackground = true, name = "Register Screen - Light", heightDp = 1100)
@Composable
fun PreviewRegisterScreen() {
    MyApplicationTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(52.dp))
            Text("Buat Akun", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextMain)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Daftar dan bergabung di Cari.in", fontFamily = InterFontFamily, fontSize = 14.sp, color = TextSub)
            Spacer(modifier = Modifier.height(32.dp))
            CariInTextField(value = "", onValueChange = {}, label = "Nama Lengkap", placeholder = "Azriel Gunawan", leadingIcon = { Icon(Icons.Default.Person, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "", onValueChange = {}, label = "NIM", placeholder = "Nilai Induk Mahasiswa", leadingIcon = { Icon(Icons.Default.Badge, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "", onValueChange = {}, label = "No. WhatsApp", placeholder = "Nomor Anda", leadingIcon = { Icon(Icons.Default.Phone, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "", onValueChange = {}, label = "Email", placeholder = "email.kamu@gmail.com", leadingIcon = { Icon(Icons.Default.Email, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "", onValueChange = {}, label = "Password", placeholder = "Buat password", leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) }, trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "", onValueChange = {}, label = "Konfirmasi Password", placeholder = "Konfirmasi password kamu", leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) }, trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(28.dp))
            Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Blue700)) {
                Icon(Icons.Default.PersonAdd, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Buat Akun", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
                Text("  atau  ", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextSub)
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), border = BorderStroke(1.5.dp, SlateGray200)) {
                Text("G", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4285F4))
                Spacer(Modifier.width(10.dp))
                Text("Hubungkan dengan Google", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextMain)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(color = TextSub, fontFamily = InterFontFamily, fontSize = 14.sp)) { append("Sudah punya akun? ") }
                    withStyle(SpanStyle(color = Blue700, fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) { append("Masuk") }
                })
            }
        }
    }
}

@Preview(showBackground = true, name = "Register - Password Tidak Sama", heightDp = 1100)
@Composable
fun PreviewRegisterValidasi() {
    MyApplicationTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SlateWhite)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(52.dp))
            Text("Buat Akun", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = TextMain)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Daftar dan bergabung di Cari.in", fontFamily = InterFontFamily, fontSize = 14.sp, color = TextSub)
            Spacer(modifier = Modifier.height(32.dp))
            CariInTextField(value = "Azriel Gunawan", onValueChange = {}, label = "Nama Lengkap", placeholder = "Azriel Gunawan", leadingIcon = { Icon(Icons.Default.Person, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "123456789", onValueChange = {}, label = "NIM", placeholder = "Nilai Induk Mahasiswa", leadingIcon = { Icon(Icons.Default.Badge, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "08123456789", onValueChange = {}, label = "No. WhatsApp", placeholder = "Nomor Anda", leadingIcon = { Icon(Icons.Default.Phone, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "azriel@gmail.com", onValueChange = {}, label = "Email", placeholder = "email.kamu@gmail.com", leadingIcon = { Icon(Icons.Default.Email, null, tint = TextHint) })
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(value = "password123", onValueChange = {}, label = "Password", placeholder = "Buat password", leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) }, trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) }, visualTransformation = PasswordVisualTransformation())
            Spacer(modifier = Modifier.height(16.dp))

            CariInTextField(value = "password999", onValueChange = {}, label = "Konfirmasi Password", placeholder = "Konfirmasi password kamu", leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) }, trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) }, visualTransformation = PasswordVisualTransformation(), isError = true)
            Text(
                text       = "Password dan konfirmasi password tidak sama",
                color      = DangerRed,
                fontFamily = InterFontFamily,
                fontSize   = 12.sp,
                modifier   = Modifier.padding(top = 6.dp)
            )
        }
    }
}