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
import com.uas.myapplication.presentation.onboarding.IlustrasiTemukan
import com.uas.myapplication.presentation.ui.theme.*

// =============================================
// LOGIN SCREEN
// =============================================
@Composable
fun LoginScreen(
    viewModel     : LoginViewModel,
    onLoginSuccess: (isAdmin: Boolean) -> Unit,
    onLengkapiProfil: () -> Unit,
    onDaftarClick : () -> Unit
) { 
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context  = LocalContext.current

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

            // Ilustrasi atas
            IlustrasiLoginBadge()

            Spacer(modifier = Modifier.height(24.dp))

            // Judul
            Text(
                text       = "Selamat Datang",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle — sudah diganti dari "Lost & Found" ke "Cari.in"
            Text(
                text       = "Masuk untuk mengakses Cari.in",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 14.sp,
                color      = TextSub,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Email
            CariInTextField(
                value         = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label         = "Email",
                placeholder   = "email.kamu@gmail.com",
                leadingIcon   = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = TextHint)
                },
                keyboardType  = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Form Password dengan toggle show/hide
            CariInTextField(
                value         = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label         = "Password",
                placeholder   = "Masukkan password",
                leadingIcon   = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = TextHint)
                },
                trailingIcon  = {
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.passwordVisible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (uiState.passwordVisible)
                                "Sembunyikan password" else "Tampilkan password",
                            tint = TextHint
                        )
                    }
                },
                visualTransformation = if (uiState.passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password
            )

            // Pesan error
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter   = fadeIn(),
                exit    = fadeOut()
            ) {
                Text(
                    text       = uiState.errorMessage ?: "",
                    color      = DangerRed,
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    modifier   = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tombol Masuk
            Button(
                onClick  = viewModel::loginWithEmail,
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = Blue700,
                    disabledContainerColor = Blue700.copy(alpha = 0.7f)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.Lock,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = "Masuk",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 16.sp,
                        color      = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider "atau lanjutkan dengan"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
                Text(
                    text       = "  atau lanjutkan dengan  ",
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    color      = TextSub
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = SlateGray200)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Masuk dengan Google
            OutlinedButton(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(context.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)
                    googleLauncher.launch(googleSignInClient.signInIntent)
                },
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                border = BorderStroke(width = 1.5.dp, color = SlateGray200)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = Blue700,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text       = "G",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 18.sp,
                        color      = Color(0xFF4285F4)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text       = "Masuk dengan Google",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 15.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Teks "Tidak mempunyai akun? Buat Akun"
            TextButton(onClick = onDaftarClick) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(
                            color      = TextSub,
                            fontFamily = InterFontFamily,
                            fontSize   = 14.sp
                        )) { append("Tidak mempunyai akun? ") }
                        withStyle(SpanStyle(
                            color      = Blue700,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize   = 14.sp
                        )) { append("Buat Akun") }
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// =============================================
// ILUSTRASI BADGE LOGIN
// =============================================
@Composable
fun IlustrasiLoginBadge() {
    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(160.dp)
    ) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(Blue100, CircleShape)
        )
        Box(modifier = Modifier.size(70.dp)) {
            IlustrasiTemukan()
        }
    }
}

// =============================================
// KOMPONEN TEXT FIELD REUSABLE
// =============================================
@Composable
fun CariInTextField(
    value                : String,
    onValueChange        : (String) -> Unit,
    label                : String,
    placeholder          : String,
    leadingIcon          : @Composable (() -> Unit)? = null,
    trailingIcon         : @Composable (() -> Unit)? = null,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    keyboardType         : KeyboardType = KeyboardType.Text,
    isError              : Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text       = label,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize   = 13.sp,
            color      = MaterialTheme.colorScheme.onBackground,
            modifier   = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = {
                Text(text = placeholder, fontFamily = InterFontFamily, fontSize = 14.sp, color = TextHint)
            },
            leadingIcon          = leadingIcon,
            trailingIcon         = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions      = KeyboardOptions(keyboardType = keyboardType),
            isError              = isError,
            singleLine           = true,
            modifier             = Modifier.fillMaxWidth(),
            shape                = RoundedCornerShape(12.dp),
            colors               = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = Blue700,
                unfocusedBorderColor = SlateGray200,
                focusedLabelColor    = Blue700,
                cursorColor          = Blue700
            )
        )
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
            IlustrasiLoginBadge()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Selamat Datang",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = TextMain
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Masuk untuk mengakses Cari.in",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSub,
                textAlign  = TextAlign.Center
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
            IlustrasiLoginBadge()
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Selamat Datang",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 24.sp,
                color      = TextMainDark
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                "Masuk untuk mengakses Cari.in",
                fontFamily = InterFontFamily,
                fontSize   = 14.sp,
                color      = TextSubDark,
                textAlign  = TextAlign.Center
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