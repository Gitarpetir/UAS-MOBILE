package com.uas.myapplication.presentation.auth.register.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.auth.register.RegisterUiState
import com.uas.myapplication.presentation.auth.register.RegisterViewModel
import com.uas.myapplication.presentation.ui.StringProvider
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun RegisterForm(
    uiState: RegisterUiState,
    viewModel: RegisterViewModel,
    modifier: Modifier = Modifier
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Column(modifier = modifier) {
        // 1. Nama Lengkap
        CariInTextField(
            value         = uiState.namaLengkap,
            onValueChange = viewModel::onNamaLengkapChange,
            label         = strings.fullNameLabel,
            placeholder   = strings.fullNamePlaceholder,
            leadingIcon   = {
                Icon(Icons.Default.Person, contentDescription = null, tint = CariInTheme.colors.textHint)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. NIM
        CariInTextField(
            value         = uiState.nim,
            onValueChange = viewModel::onNimChange,
            label         = strings.nimLabel,
            placeholder   = strings.nimPlaceholder,
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
            label         = strings.waLabel,
            placeholder   = strings.waPlaceholder,
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
            label         = strings.emailLabel,
            placeholder   = strings.emailPlaceholder,
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
            label         = strings.passwordLabel,
            placeholder   = strings.createPasswordPlaceholder,
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
            label         = strings.confirmPasswordLabel,
            placeholder   = strings.confirmPasswordPlaceholder,
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
                text       = strings.passwordMismatchError,
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
                    text       = strings.btnRegister,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
