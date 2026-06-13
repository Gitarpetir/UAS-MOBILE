package com.uas.myapplication.presentation.auth.login.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.auth.login.LoginUiState
import com.uas.myapplication.presentation.auth.login.LoginViewModel
import com.uas.myapplication.presentation.ui.StringProvider
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.components.CariInTextField
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.theme.PoppinsFontFamily

@Composable
fun LoginForm(
    uiState: LoginUiState,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Column(modifier = modifier) {
        // Form Email
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

        // Form Password dengan toggle show/hide
        CariInTextField(
            value         = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label         = strings.passwordLabel,
            placeholder   = strings.passwordPlaceholder,
            leadingIcon   = {
                Icon(Icons.Default.Lock, contentDescription = null, tint = CariInTheme.colors.textHint)
            },
            trailingIcon  = {
                IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                    Icon(
                        imageVector = if (uiState.passwordVisible)
                            Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (uiState.passwordVisible)
                            strings.hidePassword else strings.showPassword,
                        tint = CariInTheme.colors.textHint
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
                color      = MaterialTheme.colorScheme.error,
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
                    imageVector        = Icons.Default.Lock,
                    contentDescription = null,
                    tint               = MaterialTheme.colorScheme.onPrimary,
                    modifier           = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text       = strings.btnLogin,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
