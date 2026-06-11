package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import com.uas.myapplication.presentation.ui.theme.InterFontFamily

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
        if (label.isNotEmpty()) {
            Text(
                text       = label,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize   = 13.sp,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.padding(bottom = 6.dp)
            )
        }
        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            placeholder          = {
                Text(text = placeholder, fontFamily = InterFontFamily, fontSize = 14.sp, color = CariInTheme.colors.textHint)
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
                focusedBorderColor   = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor    = MaterialTheme.colorScheme.primary,
                cursorColor          = MaterialTheme.colorScheme.primary
            )
        )
    }
}
