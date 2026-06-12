package com.uas.myapplication.presentation.katalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uas.myapplication.presentation.katalog.FilterKatalog
import com.uas.myapplication.presentation.ui.components.CariInFilterChip
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun FilterChipRow(
    filterAktif   : FilterKatalog,
    onFilterChange: (FilterKatalog) -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)
    Row(
        modifier              = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CariInFilterChip(label = strings.filterAll,    isSelected = filterAktif == FilterKatalog.SEMUA,     onClick = { onFilterChange(FilterKatalog.SEMUA) })
        CariInFilterChip(label = strings.filterLost,   isSelected = filterAktif == FilterKatalog.HILANG,    onClick = { onFilterChange(FilterKatalog.HILANG) })
        CariInFilterChip(label = strings.filterFound,isSelected = filterAktif == FilterKatalog.DITEMUKAN, onClick = { onFilterChange(FilterKatalog.DITEMUKAN) })
    }
}
