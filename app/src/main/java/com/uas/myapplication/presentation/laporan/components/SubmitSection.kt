package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.uas.myapplication.presentation.ui.theme.*

@Composable
fun SubmitSection(
    isLoading: Boolean,
    isEditMode: Boolean,
    onSubmit: () -> Unit
) {

    Button(
        onClick = onSubmit,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue700,
            disabledContainerColor = Blue700.copy(alpha = 0.7f)
        )
    ) {

        if (isLoading) {

            CircularProgressIndicator(
                modifier = Modifier.size(22.dp),
                color = Color.White,
                strokeWidth = 2.5.dp
            )

        } else {

            Icon(
                imageVector =
                    if (isEditMode)
                        Icons.Default.Save
                    else
                        Icons.Default.Send,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text =
                    if (isEditMode)
                        "Simpan Perubahan"
                    else
                        "Kirim Laporan",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubmitBaru() {
    MyApplicationTheme {
        SubmitSection(
            isLoading = false,
            isEditMode = false,
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubmitEdit() {
    MyApplicationTheme {
        SubmitSection(
            isLoading = false,
            isEditMode = true,
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewSubmitLoading() {
    MyApplicationTheme {
        SubmitSection(
            isLoading = true,
            isEditMode = false,
            onSubmit = {}
        )
    }
}