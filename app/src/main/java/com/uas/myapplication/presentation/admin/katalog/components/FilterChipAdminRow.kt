package com.uas.myapplication.presentation.admin.katalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.admin.katalog.FilterAdmin
import com.uas.myapplication.presentation.ui.components.CariInFilterChip

@Composable
fun FilterChipAdminRow(
    filterAktif: FilterAdmin,
    onFilterChange: (FilterAdmin) -> Unit
) {
    val strings = com.uas.myapplication.presentation.ui.StringProvider.get(com.uas.myapplication.presentation.ui.LocalBahasa.current)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CariInFilterChip(
            label = strings.filterAll,
            isSelected = filterAktif == FilterAdmin.SEMUA,
            onClick = {
                onFilterChange(FilterAdmin.SEMUA)
            }
        )

        CariInFilterChip(
            label = strings.filterLost,
            isSelected = filterAktif == FilterAdmin.HILANG,
            onClick = {
                onFilterChange(FilterAdmin.HILANG)
            }
        )

        CariInFilterChip(
            label = strings.filterFound,
            isSelected = filterAktif == FilterAdmin.DITEMUKAN,
            onClick = {
                onFilterChange(FilterAdmin.DITEMUKAN)
            }
        )

        CariInFilterChip(
            label = strings.filterCompleted,
            isSelected = filterAktif == FilterAdmin.SELESAI,
            onClick = {
                onFilterChange(FilterAdmin.SELESAI)
            }
        )
    }
}
