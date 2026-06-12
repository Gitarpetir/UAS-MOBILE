package com.uas.myapplication.presentation.laporan.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider
import java.util.Locale

@Composable
fun StatusBarangSelector(
    selectedStatus: StatusBarang,
    onStatusChange: (StatusBarang) -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    Text(
        text = strings.selectStatus,
        fontFamily = InterFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        color = MaterialTheme.colorScheme.onBackground
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = {
                onStatusChange(StatusBarang.HILANG)
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    if (selectedStatus == StatusBarang.HILANG)
                        DangerRed
                    else
                        MaterialTheme.colorScheme.surface,

                contentColor =
                    if (selectedStatus == StatusBarang.HILANG)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                strings.statusLost.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = {
                onStatusChange(StatusBarang.DITEMUKAN)
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =
                    if (selectedStatus == StatusBarang.DITEMUKAN)
                        SuccessGreen
                    else
                        MaterialTheme.colorScheme.surface,

                contentColor =
                    if (selectedStatus == StatusBarang.DITEMUKAN)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                strings.statusFound.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStatusHilang() {
    MyApplicationTheme {
        StatusBarangSelector(
            selectedStatus = StatusBarang.HILANG,
            onStatusChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewStatusDitemukan() {
    MyApplicationTheme {
        StatusBarangSelector(
            selectedStatus = StatusBarang.DITEMUKAN,
            onStatusChange = {}
        )
    }
}