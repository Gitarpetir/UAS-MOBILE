package com.uas.myapplication.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.automirrored.outlined.*
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

import com.uas.myapplication.presentation.ui.Strings
import com.uas.myapplication.presentation.ui.StringProvider
import com.uas.myapplication.presentation.ui.LocalBahasa

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
fun getMahasiswaBottomNavItems(strings: Strings) = listOf(
    BottomNavItem(
        label       = strings.navHome,
        route       = Screen.Dashboard.route,
        iconAktif   = Icons.Filled.Home,
        iconNonAktif = Icons.Outlined.Home
    ),
    BottomNavItem(
        label       = strings.navCatalog,
        route       = Screen.Katalog.route,
        iconAktif   = Icons.AutoMirrored.Filled.List,
        iconNonAktif = Icons.AutoMirrored.Outlined.List
    ),
    BottomNavItem(
        label       = strings.navReport,
        route       = Screen.BuatLaporan.route,
        iconAktif   = Icons.Filled.AddCircle,
        iconNonAktif = Icons.Outlined.AddCircle
    ),
    BottomNavItem(
        label       = strings.navMyReports,
        route       = Screen.Laporanku.route,
        iconAktif   = Icons.AutoMirrored.Filled.Article,
        iconNonAktif = Icons.AutoMirrored.Outlined.Article
    ),
    BottomNavItem(
        label       = strings.navProfile,
        route       = Screen.Profil.route,
        iconAktif   = Icons.Filled.Person,
        iconNonAktif = Icons.Outlined.Person
    )
)

fun getAdminBottomNavItems(strings: Strings) = listOf(
    BottomNavItem(
        label = strings.navHome,
        route = Screen.DashboardAdmin.route,
        iconAktif = Icons.Filled.Home,
        iconNonAktif = Icons.Outlined.Home
    ),

    BottomNavItem(
        label = strings.navCatalog,
        route = Screen.KatalogAdmin.route,
        iconAktif = Icons.AutoMirrored.Filled.List,
        iconNonAktif = Icons.AutoMirrored.Outlined.List
    ),

    BottomNavItem(
        label = strings.navProfile,
        route = Screen.ProfilAdmin.route,
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
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector        = if (isSelected) item.iconAktif else item.iconNonAktif,
                        contentDescription = item.label,
                        modifier           = Modifier.size(24.dp),
                        tint               = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    Text(
                        text       = item.label,
                        fontFamily = InterFontFamily,
                        fontSize   = 10.sp,
                        color      = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
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
            items = getMahasiswaBottomNavItems(StringProvider.get("id"))
        )
    }
}