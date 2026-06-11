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
import com.uas.myapplication.presentation.auth.login.CariInTextField
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

            Text(
                text       = "Buat Akun",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 28.sp,
                color      = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text       = "Daftar dan bergabung di Cari.in",
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 14.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Nama Lengkap
            CariInTextField(
                value         = uiState.namaLengkap,
                onValueChange = viewModel::onNamaLengkapChange,
                label         = "Nama Lengkap",
                placeholder   = "Azriel Gunawan",
                leadingIcon   = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = CariInTheme.colors.textHint)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. NIM
            CariInTextField(
                value         = uiState.nim,
                onValueChange = viewModel::onNimChange,
                label         = "NIM",
                placeholder   = "Nilai Induk Mahasiswa",
                leadingIcon   = {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = CariInTheme.colors.textHint)
                },
                keyboardType  = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Nomor WhatsApp
            CariInTextField(
                value         = uiState.nomorWhatsapp,
                onValueChange = viewModel::onNomorWhatsappChange,
                label         = "No. WhatsApp",
                placeholder   = "Nomor Anda",
                leadingIcon   = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = CariInTheme.colors.textHint)
                },
                keyboardType  = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Email
            CariInTextField(
                value         = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label         = "Email",
                placeholder   = "email.kamu@gmail.com",
                leadingIcon   = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = CariInTheme.colors.textHint)
                },
                keyboardType  = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Password
            CariInTextField(
                value         = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label         = "Password",
                placeholder   = "Buat password",
                leadingIcon   = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CariInTheme.colors.textHint)
                },
                trailingIcon  = {
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.passwordVisible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = CariInTheme.colors.textHint
                        )
                    }
                },
                visualTransformation = if (uiState.passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Konfirmasi Password
            CariInTextField(
                value         = uiState.konfirmasiPassword,
                onValueChange = viewModel::onKonfirmasiPasswordChange,
                label         = "Konfirmasi Password",
                placeholder   = "Konfirmasi password kamu",
                leadingIcon   = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = CariInTheme.colors.textHint)
                },
                trailingIcon  = {
                    IconButton(onClick = viewModel::onToggleKonfirmasiPasswordVisibility) {
                        Icon(
                            imageVector = if (uiState.konfirmasiPasswordVisible)
                                Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = null,
                            tint = CariInTheme.colors.textHint
                        )
                    }
                },
                visualTransformation = if (uiState.konfirmasiPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                isError      = uiState.passwordTidakSama
            )

            // Peringatan password tidak sama
            AnimatedVisibility(
                visible = uiState.passwordTidakSama,
                enter   = fadeIn(),
                exit    = fadeOut()
            ) {
                Text(
                    text       = "Password dan konfirmasi password tidak sama",
                    color      = MaterialTheme.colorScheme.error,
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    modifier   = Modifier.fillMaxWidth().padding(top = 6.dp)
                )
            }

            // Pesan error umum
            AnimatedVisibility(
                visible = uiState.errorMessage != null && !uiState.passwordTidakSama,
                enter   = fadeIn(),
                exit    = fadeOut()
            ) {
                Text(
                    text       = uiState.errorMessage ?: "",
                    color      = MaterialTheme.colorScheme.error,
                    fontFamily = InterFontFamily,
                    fontSize   = 12.sp,
                    modifier   = Modifier.fillMaxWidth().padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Tombol Buat Akun
            Button(
                onClick  = viewModel::register,
                enabled  = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(16.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        imageVector        = Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint               = MaterialTheme.colorScheme.onPrimary,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text       = "Buat Akun",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize   = 16.sp,
                        color      = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Divider "atau"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier          = Modifier.fillMaxWidth()
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
                Text("  atau  ", fontFamily = InterFontFamily, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Tombol Hubungkan dengan Google
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
                border = BorderStroke(width = 1.5.dp, color = MaterialTheme.colorScheme.outline)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(22.dp),
                        color       = MaterialTheme.colorScheme.primary,
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
                        text       = "Hubungkan dengan Google",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize   = 15.sp,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

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
                            )) { append("Sudah punya akun? ") }
                            withStyle(SpanStyle(
                                color      = MaterialTheme.colorScheme.primary,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 14.sp
                            )) { append("Masuk") }
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