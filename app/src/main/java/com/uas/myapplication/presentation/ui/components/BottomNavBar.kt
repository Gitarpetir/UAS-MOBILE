package com.uas.myapplication.presentation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.theme.*

// =============================================
// DATA MODEL ITEM NAVIGASI
// =============================================
data class BottomNavItem(
    val label      : String,
    val route      : String,
    val iconAktif  : ImageVector,
    val iconNonAktif: ImageVector
)

// Daftar 5 item bottom navigation
val mahasiswaBottomNavItems = listOf(
    BottomNavItem(
        label       = "Beranda",
        route       = Screen.Dashboard.route,
        iconAktif   = Icons.Filled.Home,
        iconNonAktif = Icons.Outlined.Home
    ),
    BottomNavItem(
        label       = "Katalog",
        route       = Screen.Katalog.route,
        iconAktif   = Icons.Filled.List,
        iconNonAktif = Icons.Outlined.List
    ),
    BottomNavItem(
        label       = "Laporan",
        route       = Screen.BuatLaporan.route,
        iconAktif   = Icons.Filled.AddCircle,
        iconNonAktif = Icons.Outlined.AddCircle
    ),
    BottomNavItem(
        label       = "Laporanku",
        route       = Screen.Laporanku.route,
        iconAktif   = Icons.Filled.Article,
        iconNonAktif = Icons.Outlined.Article
    ),
    BottomNavItem(
        label       = "Profil",
        route       = Screen.Profil.route,
        iconAktif   = Icons.Filled.Person,
        iconNonAktif = Icons.Outlined.Person
    )
)

val adminBottomNavItems = listOf(

    BottomNavItem(
        label = "Beranda",
        route = Screen.DashboardAdmin.route,
        iconAktif = Icons.Filled.Home,
        iconNonAktif = Icons.Outlined.Home
    ),

    BottomNavItem(
        label = "Laporan",
        route = Screen.LaporanAdmin.route,
        iconAktif = Icons.Filled.Article,
        iconNonAktif = Icons.Outlined.Article
    ),

    BottomNavItem(
        label = "Profil",
        route = Screen.Profil.route,
        iconAktif = Icons.Filled.Person,
        iconNonAktif = Icons.Outlined.Person
    )
)

// ================ =============================
// KOMPONEN BOTTOM NAVIGATION BAR
// =============================================
@Composable
fun CariInBottomNavBar(
    navController: NavController,
    items: List<BottomNavItem>) {
    // Ambil route yang sedang aktif
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick  = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            // Kembali ke Dashboard saat pindah tab
                            // agar back stack tidak menumpuk
                            popUpTo(Screen.Dashboard.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState    = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector        = if (isSelected) item.iconAktif else item.iconNonAktif,
                        contentDescription = item.label,
                        modifier           = Modifier.size(24.dp),
                        tint               = if (isSelected) Blue700
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text       = item.label,
                        fontFamily = InterFontFamily,
                        fontSize   = 10.sp,
                        color      = if (isSelected) Blue700
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Blue100
                )
            )
        }
    }
}

// =============================================
// PREVIEW
// =============================================
@Preview(showBackground = true)
@Composable
fun PreviewBottomNavBar() {
    MyApplicationTheme {
        CariInBottomNavBar(
            navController = rememberNavController(),
            items = mahasiswaBottomNavItems
        )
    }
}