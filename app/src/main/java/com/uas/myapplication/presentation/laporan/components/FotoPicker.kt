package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.core.net.toUri
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun FotoPicker(
    fotoUri: String,
    fotoUrl: String,
    onClick: () -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Text(
        text = strings.photoLabel,
        fontFamily = InterFontFamily
    )

    Spacer(modifier = Modifier.height(8.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.5.dp,
                SlateGray200,
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        when {

            fotoUri.isNotEmpty() -> {

                AsyncImage(
                    model = fotoUri.toUri(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            fotoUrl.isNotEmpty() -> {

                AsyncImage(
                    model = fotoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Upload,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = strings.tapToUpload,
                        fontFamily = InterFontFamily,
                        color = TextHint
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFotoPickerKosong() {
    MyApplicationTheme {
        FotoPicker(
            fotoUri = "",
            fotoUrl = "",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFotoPickerAdaFoto() {
    MyApplicationTheme {
        FotoPicker(
            fotoUri = "",
            fotoUrl = "https://picsum.photos/600",
            onClick = {}
        )
    }
}