@file:Suppress("DEPRECATION")
package com.uas.myapplication.presentation.auth.login

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.uas.myapplication.R
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.auth.components.AuthDivider
import com.uas.myapplication.presentation.auth.components.AuthHeader
import com.uas.myapplication.presentation.auth.components.GoogleAuthButton
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme

@Composable
fun LoginScreen(
    viewModel     : LoginViewModel,
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onLengkapiProfil: () -> Unit,
    onDaftarClick : () -> Unit
) { 
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context  = LocalContext.current
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)

    // Launcher untuk Google Sign-In
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

    // Navigasi setelah login berhasil
    LaunchedEffect(uiState.loginSuccess) {
        if (uiState.loginSuccess) {
            onLoginSuccess(uiState.isAdmin)
            viewModel.resetLoginSuccess()
        }
    }

    LaunchedEffect(uiState.needsCompleteProfile) {
        if (uiState.needsCompleteProfile) {
            onLengkapiProfil()
            viewModel.resetNeedsCompleteProfile()
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            AuthHeader(
                title = strings.welcome,
                subtitle = strings.loginSubtitle
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Email & Password
            com.uas.myapplication.presentation.auth.login.components.LoginForm(
                uiState = uiState,
                viewModel = viewModel
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Divider "atau lanjutkan dengan"
            AuthDivider(label = strings.orContinueWith)

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Masuk dengan Google
            GoogleAuthButton(
                label = strings.btnGoogleLogin,
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

            // Teks "Tidak mempunyai akun? Buat Akun"
            TextButton(onClick = onDaftarClick) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(
                            color      = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp
                        )) { append(strings.noAccountText) }
                        withStyle(SpanStyle(
                            color      = MaterialTheme.colorScheme.primary,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )) { append(strings.btnCreateAccount) }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



// =============================================
// PREVIEW — heightDp ditambah agar tidak terpotong
// =============================================
@Preview(showBackground = true, name = "Login Screen - Light", heightDp = 900)
@Composable
fun PreviewLoginScreen() {
    MyApplicationTheme {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .background(SlateWhite)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            AuthHeader(
                title = "Selamat Datang",
                subtitle = "Masuk untuk mengakses Cari.in"
            )
            Spacer(modifier = Modifier.height(32.dp))
            CariInTextField(
                value = "", onValueChange = {}, label = "Email",
                placeholder = "email.kamu@gmail.com",
                leadingIcon = { Icon(Icons.Default.Email, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value = "", onValueChange = {}, label = "Password",
                placeholder = "Masukkan password",
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) },
                trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
            ) {
                Icon(Icons.Default.Lock, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Masuk", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
                Text("  atau lanjutkan dengan  ", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextSub)
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                border   = BorderStroke(1.5.dp, SlateGray200)
            ) {
                Text("G", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4285F4))
                Spacer(Modifier.width(10.dp))
                Text("Masuk dengan Google", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextMain)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextSub, fontFamily = InterFontFamily, fontSize = 14.sp)) { append("Tidak mempunyai akun? ") }
                    withStyle(SpanStyle(color = Blue700, fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) { append("Buat Akun") }
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Login Screen - Dark", heightDp = 900)
@Composable
fun PreviewLoginScreenDark() {
    MyApplicationTheme(darkTheme = true) {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            AuthHeader(
                title = "Selamat Datang",
                subtitle = "Masuk untuk mengakses Cari.in"
            )
            Spacer(modifier = Modifier.height(32.dp))
            CariInTextField(
                value = "", onValueChange = {}, label = "Email",
                placeholder = "email.kamu@gmail.com",
                leadingIcon = { Icon(Icons.Default.Email, null, tint = TextHint) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            CariInTextField(
                value = "", onValueChange = {}, label = "Password",
                placeholder = "Masukkan password",
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextHint) },
                trailingIcon = { Icon(Icons.Default.VisibilityOff, null, tint = TextHint) },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(containerColor = Blue700)
            ) {
                Icon(Icons.Default.Lock, null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Masuk", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = DarkSurface2)
                Text("  atau lanjutkan dengan  ", fontFamily = InterFontFamily, fontSize = 12.sp, color = TextSubDark)
                HorizontalDivider(modifier = Modifier.weight(1f), color = DarkSurface2)
            }
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedButton(
                onClick  = {},
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                border   = BorderStroke(1.5.dp, DarkSurface2)
            ) {
                Text("G", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF4285F4))
                Spacer(Modifier.width(10.dp))
                Text("Masuk dengan Google", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium, fontSize = 15.sp, color = TextMainDark)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextSubDark, fontFamily = InterFontFamily, fontSize = 14.sp)) { append("Tidak mempunyai akun? ") }
                    withStyle(SpanStyle(color = Blue700, fontFamily = InterFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)) { append("Buat Akun") }
                }
            )
        }
    }
}