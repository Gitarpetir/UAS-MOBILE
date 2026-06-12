package com.uas.myapplication.presentation.profil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.uas.myapplication.data.local.PreferensiManager
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.presentation.navigation.Screen
import com.uas.myapplication.presentation.ui.components.CariInBottomNavBar
import com.uas.myapplication.presentation.ui.components.getAdminBottomNavItems
import com.uas.myapplication.presentation.ui.components.getMahasiswaBottomNavItems
import com.uas.myapplication.presentation.profil.components.BahasaDialog
import com.uas.myapplication.presentation.profil.components.DataCard
import com.uas.myapplication.presentation.profil.components.LogoutButton
import com.uas.myapplication.presentation.profil.components.ProfilAvatar
import com.uas.myapplication.presentation.profil.components.ProfilPreferensiSection
import com.uas.myapplication.presentation.ui.theme.*
import com.uas.myapplication.presentation.ui.theme.CariInTheme
import com.uas.myapplication.presentation.ui.LocalBahasa
import com.uas.myapplication.presentation.ui.StringProvider

// =============================================
// PROFIL SCREEN
// =============================================
@Composable
fun ProfilScreen(
    viewModel    : ProfilViewModel,
    navController: NavController,
    isAdmin      : Boolean = false
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val strings = StringProvider.get(LocalBahasa.current)

    // Navigasi ke Login setelah logout
    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            viewModel.resetLogoutSuccess()
        }
    }

    // Dialog pilihan bahasa
    if (uiState.showBahasaDialog) {
        BahasaDialog(
            bahasaAktif = uiState.bahasa,
            onPilih     = viewModel::onPilihBahasa,
            onBatal     = viewModel::onBatalBahasaDialog
        )
    }

    Scaffold(
        bottomBar = {
            CariInBottomNavBar(
                navController = navController,
                items = if (isAdmin) getAdminBottomNavItems(strings) else getMahasiswaBottomNavItems(strings)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // =============================================
            // AVATAR
            // =============================================
            ProfilAvatar(
                namaLengkap = uiState.user?.namaLengkap ?: "..."
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color    = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // =============================================
            // SECTION DATA PENGGUNA
            // =============================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text       = strings.userDataSection,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Card Email
                DataCard(
                    icon  = Icons.Default.Email,
                    label = strings.emailLabel,
                    value = uiState.user?.email ?: "..."
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Card NIM
                DataCard(
                    icon  = Icons.Default.Badge,
                    label = strings.nimLabel,
                    value = uiState.user?.nim ?: "..."
                )

                Spacer(modifier = Modifier.height(8.dp))

                DataCard(
                    icon = Icons.Default.Phone,
                    label = strings.waLabel,
                    value = uiState.user?.nomorWhatsapp ?: "-"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // =============================================
                // SECTION PREFERENSI
                // =============================================
                ProfilPreferensiSection(
                    title = strings.preferencesSection,
                    darkModeLabel = strings.darkModeLabel,
                    languageLabel = strings.languageLabel,
                    langIndonesian = strings.langIndonesian,
                    langEnglish = strings.langEnglish,
                    isDarkMode = uiState.isDarkMode,
                    bahasa = uiState.bahasa,
                    onDarkModeToggle = viewModel::onDarkModeToggle,
                    onBahasaClick = viewModel::onBahasaClick
                )

                Spacer(modifier = Modifier.height(32.dp))

                // =============================================
                // TOMBOL KELUAR
                // =============================================
                LogoutButton(
                    label = strings.btnLogout,
                    onClick = viewModel::onLogout
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}



@Preview(showBackground = true, name = "Profil Screen - Light", heightDp = 800)
@Composable
fun PreviewProfilScreen() {
    MyApplicationTheme {
        Column(
            modifier            = Modifier
                .fillMaxSize()
                .background(SlateWhite),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier         = Modifier.size(90.dp).background(Blue100, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Blue700, modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Azriel Gunawan", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextMain)

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = SlateGray100)

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("Data Pengguna", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextMain)

                Spacer(modifier = Modifier.height(12.dp))

                DataCard(icon = Icons.Default.Email, label = "Email", value = "azrielgunawan@gmail.com")

                Spacer(modifier = Modifier.height(8.dp))

                DataCard(icon = Icons.Default.Badge, label = "NIM", value = "2410817110009")
                Spacer(modifier = Modifier.height(8.dp))

                DataCard(
                    icon = Icons.Default.Phone,
                    label = "Nomor WhatsApp",
                    value = "081251080786"
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text("Preferensi", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextMain)

                Spacer(modifier = Modifier.height(12.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DarkMode, null, tint = Blue700, modifier = Modifier.size(20.dp))

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("Mode Gelap", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextMain, modifier = Modifier.weight(1f))
                        Switch(checked = false, onCheckedChange = {}, colors = SwitchDefaults.colors(uncheckedTrackColor = SlateGray200))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(1.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, null, tint = Blue700, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Bahasa", fontFamily = InterFontFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = TextMain, modifier = Modifier.weight(1f))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Indonesia", fontFamily = InterFontFamily, fontSize = 13.sp, color = Blue700)
                            Icon(Icons.Default.ChevronRight, null, tint = Blue700, modifier = Modifier.size(18.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {}, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = DangerRed)) {
                    Icon(Icons.Default.Logout, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Keluar", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Profil Screen - Dark", heightDp = 800)
@Composable
fun PreviewProfilScreenDark() {
    MyApplicationTheme(darkTheme = true) {
        Column(
            modifier            = Modifier.fillMaxSize().background(DarkBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.size(90.dp).background(Blue800, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Blue100, modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Azriel Gunawan", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextMainDark)
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = DarkSurface2)
            Spacer(modifier = Modifier.height(24.dp))
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                Text("Data Pengguna", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = TextMainDark)
                Spacer(modifier = Modifier.height(12.dp))
                DataCard(icon = Icons.Default.Email, label = "Email", value = "azrielgunawan@gmail.com")
                Spacer(modifier = Modifier.height(8.dp))
                DataCard(icon = Icons.Default.Badge, label = "NIM", value = "2410817110009")
            }
        }
    }
}

@Preview(showBackground = true, name = "Dialog Bahasa")
@Composable
fun PreviewBahasaDialog() {
    MyApplicationTheme {
        BahasaDialog(bahasaAktif = "id", onPilih = {}, onBatal = {})
    }
}