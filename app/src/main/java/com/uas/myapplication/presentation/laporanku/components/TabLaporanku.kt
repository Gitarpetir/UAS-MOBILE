package com.uas.myapplication.presentation.laporanku.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.uas.myapplication.presentation.laporanku.TabLaporanku
import com.uas.myapplication.presentation.ui.theme.Blue700
import com.uas.myapplication.presentation.ui.theme.InterFontFamily
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

@Composable
fun TabLaporanku(
    tabAktif: TabLaporanku,
    onTabChange: (TabLaporanku) -> Unit
) {
    val strings = StringProvider.get(LocalBahasa.current)

    val tabs = listOf(
        strings.tabMyItems,
        strings.tabMyFinds,
        strings.tabContributions
    )

    val selectedIndex = when (tabAktif) {
        TabLaporanku.BARANGKU -> 0
        TabLaporanku.TEMUANKU -> 1
        TabLaporanku.KONTRIBUSI -> 2
    }

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Blue700
    ) {

        tabs.forEachIndexed { index, title ->

            Tab(
                selected = selectedIndex == index,
                onClick = {
                    when (index) {
                        0 -> onTabChange(TabLaporanku.BARANGKU)
                        1 -> onTabChange(TabLaporanku.TEMUANKU)
                        2 -> onTabChange(TabLaporanku.KONTRIBUSI)
                    }
                },
                text = {
                    Text(
                        text = title,
                        fontFamily = InterFontFamily,
                        fontWeight = if (selectedIndex == index)
                            FontWeight.SemiBold
                        else
                            FontWeight.Normal
                    )
                }
            )
        }
    }
}