package com.uas.myapplication.presentation.ui

object StringProvider {
    fun get(bahasa: String): Strings {
        return if (bahasa == "en") EnglishStrings else IndonesianStrings
    }
}

data class Strings(
    // Onboarding
    val onboardingTitle1: String,
    val onboardingDesc1: String,
    val onboardingTitle2: String,
    val onboardingDesc2: String,
    val onboardingTitle3: String,
    val onboardingDesc3: String,
    val btnLanjut: String,
    val btnAyoMulai: String,
    val btnLewati: String,

    // Auth - Login
    val welcome: String,
    val loginSubtitle: String,
    val emailLabel: String,
    val emailPlaceholder: String,
    val passwordLabel: String,
    val passwordPlaceholder: String,
    val btnLogin: String,
    val orContinueWith: String,
    val btnGoogleLogin: String,
    val noAccountText: String,
    val btnCreateAccount: String,
    val hidePassword: String,
    val showPassword: String,

    // Auth - Register
    val registerTitle: String,
    val registerSubtitle: String,
    val fullNameLabel: String,
    val fullNamePlaceholder: String,
    val nimLabel: String,
    val nimPlaceholder: String,
    val waLabel: String,
    val waPlaceholder: String,
    val createPasswordPlaceholder: String,
    val confirmPasswordLabel: String,
    val confirmPasswordPlaceholder: String,
    val passwordMismatchError: String,
    val btnRegister: String,
    val orText: String,
    val btnGoogleConnect: String,
    val alreadyHaveAccountText: String,

    // Auth - Lengkapi Profil
    val completeProfileTitle: String,
    val completeProfileSubtitle: String,
    val btnSaveProfile: String,

    // Bottom Navigation
    val navHome: String,
    val navCatalog: String,
    val navReport: String,
    val navMyReports: String,
    val navProfile: String,
    val navAdminReports: String,

    // Dashboard Mahasiswa
    val helloGreeting: String,
    val lostReportsHeader: String,
    val foundReportsHeader: String,
    val btnReportLost: String,
    val reportLostSubtitle: String,
    val btnReportFound: String,
    val reportFoundSubtitle: String,
    val recentItemsHeader: String,
    val btnSeeAll: String,
    val noItemReportsYet: String,

    // Dashboard Admin
    val recentReportsHeader: String,
    val manageReportsHeader: String,
    val statLostItems: String,
    val statFoundItems: String,
    val statCompletedReports: String,

    // Katalog
    val catalogTitle: String,
    val searchPlaceholder: String,
    val noItemsFound: String,
    val tryChangingFilter: String,
    val filterAll: String,
    val filterLost: String,
    val filterFound: String,
    val filterCompleted: String,

    // Form Laporan
    val reportItemTitle: String,
    val statusLost: String,
    val statusFound: String,
    val selectStatus: String,
    val photoLabel: String,
    val tapToUpload: String,
    val btnUploadPhoto: String,
    val btnChangePhoto: String,
    val itemNameLabel: String,
    val itemNamePlaceholder: String,
    val itemNameRequired: String,
    val descriptionLabel: String,
    val descriptionPlaceholder: String,
    val descriptionRequired: String,
    val locationLabel: String,
    val locationPlaceholder: String,
    val locationRequired: String,
    val btnSelect: String,
    val btnCancel: String,
    val btnSave: String,
    val btnSubmitReport: String,

    // Detail Barang
    val itemDetailsTitle: String,
    val reporterInfo: String,
    val finderInfo: String,
    val dateLabel: String,
    val statusLabel: String,
    
    // Action Section Detail
    val claimItem: String,
    val verifyClaim: String,
    val confirmDelete: String,
    val btnDeleteReport: String,
    val youHaveReportedThis: String,
    val waitingForOwnerClaim: String,
    val itemHasBeenFoundAndReturned: String,
    val btnFoundThisItem: String,
    val btnThisIsMine: String,
    val btnFinishReport: String,
    val btnBack: String,
    val reporterInfo: String,
    val finderInfo: String,
    
    // Laporanku
    val tabMyItems: String,
    val tabMyFinds: String,
    val tabContributions: String,

    // Laporanku Card & Badges
    val statusBadgeLost: String,
    val statusBadgeFound: String,
    val statusBadgeCompleted: String,
    val yourItemFound: String,
    val btnEdit: String,
    val btnDelete: String,

    // Laporanku Components
    val emptyStateLostTitle: String,
    val emptyStateFoundTitle: String,
    val emptyStateContribTitle: String,
    val emptyStateLostDesc: String,
    val emptyStateFoundDesc: String,
    val emptyStateContribDesc: String,
    val reportsCountLabel: String,
    val myReportsHeader: String,
    val btnNew: String,

    // Profil
    val userDataSection: String,
    val nameLabel: String,
    val preferencesSection: String,
    val darkModeLabel: String,
    val languageLabel: String,
    val langIndonesian: String,
    val langEnglish: String,
    val btnLogout: String,
    val selectLanguageTitle: String,

    // Dialogs
    val deleteReportTitle: String,
    val deleteReportMessage: String, // requires %s for item name
    val isThisYoursTitle: String,
    val isThisYoursMessage: String,
    val btnYesItsMine: String,
    val contactFinderTitle: String,
    val contactFinderMessage: String,
    val btnGotIt: String,
    val dialogFoundTitle: String,
    val dialogFoundText: String,
    val btnYesIFoundIt: String,
    val dialogMineTitle: String,
    val dialogMineText: String,
    val btnUnderstood: String
)

private val IndonesianStrings = Strings(
    // Onboarding
    onboardingTitle1 = "Selamat Datang di Cari.in",
    onboardingDesc1 = "Aplikasi inovatif yang membantu kamu mencari barang hilang atau melaporkan barang temuan.",
    onboardingTitle2 = "Lapor Cepat & Mudah",
    onboardingDesc2 = "Kehilangan sesuatu? Buat laporan agar orang lain bisa membantu mencarinya.",
    onboardingTitle3 = "Bantu Sesama",
    onboardingDesc3 = "Menemukan barang? Jadilah pahlawan dengan melaporkannya di Cari.in.",
    btnLanjut = "Lanjut",
    btnAyoMulai = "Ayo Mulai",
    btnLewati = "Lewati",

    // Auth - Login
    welcome = "Selamat Datang",
    loginSubtitle = "Masuk untuk mengakses Cari.in",
    emailLabel = "Email",
    emailPlaceholder = "email.kamu@gmail.com",
    passwordLabel = "Password",
    passwordPlaceholder = "Masukkan password",
    btnLogin = "Masuk",
    orContinueWith = "  atau lanjutkan dengan  ",
    btnGoogleLogin = "Masuk dengan Google",
    noAccountText = "Tidak mempunyai akun? ",
    btnCreateAccount = "Buat Akun",
    hidePassword = "Sembunyikan password",
    showPassword = "Tampilkan password",

    // Auth - Register
    registerTitle = "Buat Akun",
    registerSubtitle = "Daftar dan bergabung di Cari.in",
    fullNameLabel = "Nama Lengkap",
    fullNamePlaceholder = "Azriel Gunawan",
    nimLabel = "NIM",
    nimPlaceholder = "Nilai Induk Mahasiswa",
    waLabel = "No. WhatsApp",
    waPlaceholder = "Nomor Anda",
    createPasswordPlaceholder = "Buat password",
    confirmPasswordLabel = "Konfirmasi Password",
    confirmPasswordPlaceholder = "Konfirmasi password kamu",
    passwordMismatchError = "Password dan konfirmasi password tidak sama",
    btnRegister = "Buat Akun",
    orText = "  atau  ",
    btnGoogleConnect = "Hubungkan dengan Google",
    alreadyHaveAccountText = "Sudah punya akun? ",

    // Auth - Lengkapi Profil
    completeProfileTitle = "Lengkapi Profil",
    completeProfileSubtitle = "Lengkapi data profilmu di Cari.in",
    btnSaveProfile = "Simpan Profil",

    // Bottom Navigation
    navHome = "Beranda",
    navCatalog = "Katalog",
    navReport = "Laporan",
    navMyReports = "Laporanku",
    navProfile = "Profil",
    navAdminReports = "Laporan",

    // Dashboard Mahasiswa
    helloGreeting = "Halo,",
    lostReportsHeader = "Laporan Hilang",
    foundReportsHeader = "Laporan Ditemukan",
    btnReportLost = "Lapor Kehilangan",
    reportLostSubtitle = "Barangmu hilang? Buat laporan di sini",
    btnReportFound = "Lapor Temuan",
    reportFoundSubtitle = "Menemukan barang? Bantu kembalikan",
    recentItemsHeader = "Barang Terbaru",
    btnSeeAll = "Lihat Semua",
    noItemReportsYet = "Belum ada laporan barang",

    // Dashboard Admin
    recentReportsHeader = "Laporan Terbaru",
    manageReportsHeader = "Kelola Laporan",
    statLostItems = "Barang Hilang",
    statFoundItems = "Barang Ditemukan",
    statCompletedReports = "Laporan Selesai",

    // Katalog
    catalogTitle = "Katalog Barang",
    searchPlaceholder = "Cari berdasarkan nama atau deskripsi...",
    noItemsFound = "Tidak ada barang ditemukan",
    tryChangingFilter = "Coba ubah kata kunci atau filter",
    filterAll = "Semua",
    filterLost = "Hilang",
    filterFound = "Ditemukan",
    filterCompleted = "Selesai",

    // Form Laporan
    reportItemTitle = "Laporkan Barang",
    statusLost = "hilang",
    statusFound = "ditemukan",
    selectStatus = "Pilih Status",
    photoLabel = "Foto Barang",
    tapToUpload = "Tekan untuk mengunggah foto",
    btnUploadPhoto = "Upload Foto",
    btnChangePhoto = "Ganti Foto",
    itemNameLabel = "Nama Barang",
    itemNamePlaceholder = "misalnya, Jam Hitam",
    itemNameRequired = "Nama barang wajib diisi",
    descriptionLabel = "Deskripsi",
    descriptionPlaceholder = "Berikan detail tentang barang tersebut...",
    descriptionRequired = "Deskripsi wajib diisi",
    locationLabel = "Lokasi",
    locationPlaceholder = "misalnya, Lab Komputer Dasar",
    locationRequired = "Lokasi wajib diisi",
    btnSelect = "Pilih",
    btnCancel = "Batal",
    btnSave = "Simpan",
    btnSubmitReport = "Kirim Laporan",

    // Detail Barang
    itemDetailsTitle = "Detail Barang",
    reporterInfo = "Informasi Pelapor",
    finderInfo = "Informasi Penemu",
    dateLabel = "Tanggal",
    statusLabel = "Status",

    // Action Section Detail
    claimItem = "Klaim Barang Ini",
    verifyClaim = "Verifikasi Klaim",
    confirmDelete = "Konfirmasi Penghapusan",
    btnDeleteReport = "Hapus Laporan",
    youHaveReportedThis = "Anda telah melaporkan ini",
    waitingForOwnerClaim = "Menunggu diklaim pemilik",
    itemHasBeenFoundAndReturned = "Barang telah ditemukan dan dikembalikan",
    btnFoundThisItem = "Aku Menemukan Barang Ini",
    btnThisIsMine = "Barang Ini Milik Saya",
    btnFinishReport = "Selesaikan Laporan",
    btnBack = "Kembali",
    reporterInfo = "Informasi Pelapor",
    finderInfo = "Informasi Penemu",

    // Laporanku
    tabMyItems = "Barangku",
    tabMyFinds = "Temuanku",
    tabContributions = "Kontribusi",

    // Laporanku Card & Badges
    statusBadgeLost = "Hilang",
    statusBadgeFound = "Ditemukan",
    statusBadgeCompleted = "Selesai",
    yourItemFound = "Barangmu Ditemukan",
    btnEdit = "Edit",
    btnDelete = "Hapus",

    // Laporanku Components
    emptyStateLostTitle = "Belum ada laporan kehilangan",
    emptyStateFoundTitle = "Belum ada laporan temuan",
    emptyStateContribTitle = "Belum ada kontribusi",
    emptyStateLostDesc = "Tekan tombol + Baru untuk membuat laporan kehilangan",
    emptyStateFoundDesc = "Tekan tombol + Baru untuk membuat laporan temuan",
    emptyStateContribDesc = "Kontribusimu akan muncul di sini",
    reportsCountLabel = "laporan",
    myReportsHeader = "Laporanku",
    btnNew = "Baru",

    // Profil
    userDataSection = "Data Pengguna",
    nameLabel = "Nama",
    preferencesSection = "Preferensi",
    darkModeLabel = "Mode Gelap",
    languageLabel = "Bahasa",
    langIndonesian = "Indonesia",
    langEnglish = "English",
    btnLogout = "Keluar",
    selectLanguageTitle = "Pilih Bahasa",

    // Dialogs
    deleteReportTitle = "Hapus Laporan?",
    deleteReportMessage = "Laporan \"%s\" akan dihapus secara permanen dan tidak bisa dikembalikan.",
    isThisYoursTitle = "Barang Ini Milikmu?",
    isThisYoursMessage = "Apakah benar ini adalah barangmu yang hilang? Pastikan informasi sesuai sebelum mengonfirmasi.",
    btnYesItsMine = "Ya, Ini Milik Saya",
    contactFinderTitle = "Hubungi Penemu",
    contactFinderMessage = "Silakan temui penemu barang ini untuk mengambil barangmu. Pastikan membawa bukti kepemilikan jika diperlukan.",
    btnGotIt = "Mengerti",
    dialogFoundTitle = "Konfirmasi Penemuan",
    dialogFoundText = "Apakah Anda benar menemukan barang ini? Jika ya, mohon segera serahkan barang tersebut ke Ruangan Bersama.",
    btnYesIFoundIt = "Ya, Saya Temukan",
    dialogMineTitle = "Ambil Barangmu",
    dialogMineText = "Silakan menuju Ruang Bersama untuk mengambil barang Anda dengan menunjukkan identitas.",
    btnUnderstood = "Mengerti"
)

private val EnglishStrings = Strings(
    // Onboarding
    onboardingTitle1 = "Welcome to Cari.in",
    onboardingDesc1 = "An innovative app that helps you find lost items or report found items.",
    onboardingTitle2 = "Quick & Easy Reporting",
    onboardingDesc2 = "Lost something? Create a report so others can help find it.",
    onboardingTitle3 = "Help Others",
    onboardingDesc3 = "Found an item? Be a hero by reporting it on Cari.in.",
    btnLanjut = "Next",
    btnAyoMulai = "Let's Start",
    btnLewati = "Skip",

    // Auth - Login
    welcome = "Welcome",
    loginSubtitle = "Log in to access Cari.in",
    emailLabel = "Email",
    emailPlaceholder = "your.email@gmail.com",
    passwordLabel = "Password",
    passwordPlaceholder = "Enter password",
    btnLogin = "Log In",
    orContinueWith = "  or continue with  ",
    btnGoogleLogin = "Log in with Google",
    noAccountText = "Don't have an account? ",
    btnCreateAccount = "Create Account",
    hidePassword = "Hide password",
    showPassword = "Show password",

    // Auth - Register
    registerTitle = "Create Account",
    registerSubtitle = "Sign up and join Cari.in",
    fullNameLabel = "Full Name",
    fullNamePlaceholder = "John Doe",
    nimLabel = "Student ID",
    nimPlaceholder = "Your Student ID",
    waLabel = "WhatsApp Number",
    waPlaceholder = "Your Number",
    createPasswordPlaceholder = "Create password",
    confirmPasswordLabel = "Confirm Password",
    confirmPasswordPlaceholder = "Confirm your password",
    passwordMismatchError = "Passwords do not match",
    btnRegister = "Create Account",
    orText = "  or  ",
    btnGoogleConnect = "Connect with Google",
    alreadyHaveAccountText = "Already have an account? ",

    // Auth - Lengkapi Profil
    completeProfileTitle = "Complete Profile",
    completeProfileSubtitle = "Complete your profile data on Cari.in",
    btnSaveProfile = "Save Profile",

    // Bottom Navigation
    navHome = "Home",
    navCatalog = "Catalog",
    navReport = "Report",
    navMyReports = "My Reports",
    navProfile = "Profile",
    navAdminReports = "Reports",

    // Dashboard Mahasiswa
    helloGreeting = "Hello,",
    lostReportsHeader = "Lost Reports",
    foundReportsHeader = "Found Reports",
    btnReportLost = "Report Lost Item",
    reportLostSubtitle = "Lost your item? Report it here",
    btnReportFound = "Report Found Item",
    reportFoundSubtitle = "Found an item? Help return it",
    recentItemsHeader = "Recent Items",
    btnSeeAll = "See All",
    noItemReportsYet = "No item reports yet",

    // Dashboard Admin
    recentReportsHeader = "Recent Reports",
    manageReportsHeader = "Manage Reports",
    statLostItems = "Lost Items",
    statFoundItems = "Found Items",
    statCompletedReports = "Completed Reports",

    // Katalog
    catalogTitle = "Item Catalog",
    searchPlaceholder = "Search by name or description...",
    noItemsFound = "No items found",
    tryChangingFilter = "Try changing keywords or filters",
    filterAll = "All",
    filterLost = "Lost",
    filterFound = "Found",
    filterCompleted = "Completed",

    // Form Laporan
    reportItemTitle = "Report Item",
    statusLost = "lost",
    statusFound = "found",
    selectStatus = "Select Status",
    photoLabel = "Item Photo",
    tapToUpload = "Tap to upload photo",
    btnUploadPhoto = "Upload Photo",
    btnChangePhoto = "Change Photo",
    itemNameLabel = "Item Name",
    itemNamePlaceholder = "e.g., Black Watch",
    itemNameRequired = "Item name is required",
    descriptionLabel = "Description",
    descriptionPlaceholder = "Provide details about the item...",
    descriptionRequired = "Description is required",
    locationLabel = "Location",
    locationPlaceholder = "e.g., Basic Computer Lab",
    locationRequired = "Location is required",
    btnSelect = "Select",
    btnCancel = "Cancel",
    btnSave = "Save",
    btnSubmitReport = "Submit Report",

    // Detail Barang
    itemDetailsTitle = "Item Details",
    reporterInfo = "Reporter Information",
    finderInfo = "Finder Information",
    dateLabel = "Date",
    statusLabel = "Status",

    // Action Section Detail
    claimItem = "Claim This Item",
    verifyClaim = "Verify Claim",
    confirmDelete = "Confirm Deletion",
    btnDeleteReport = "Delete Report",
    youHaveReportedThis = "You have reported this",
    waitingForOwnerClaim = "Waiting for owner claim",
    itemHasBeenFoundAndReturned = "Item has been found and returned",
    btnFoundThisItem = "I Found This Item",
    btnThisIsMine = "This Item Is Mine",
    btnFinishReport = "Finish Report",
    btnBack = "Back",
    reporterInfo = "Reporter Information",
    finderInfo = "Finder Information",

    // Laporanku
    tabMyItems = "My Items",
    tabMyFinds = "My Finds",
    tabContributions = "Contributions",

    // Laporanku Card & Badges
    statusBadgeLost = "Lost",
    statusBadgeFound = "Found",
    statusBadgeCompleted = "Completed",
    yourItemFound = "Your Item Found",
    btnEdit = "Edit",
    btnDelete = "Delete",

    // Laporanku Components
    emptyStateLostTitle = "No lost reports yet",
    emptyStateFoundTitle = "No found reports yet",
    emptyStateContribTitle = "No contributions yet",
    emptyStateLostDesc = "Tap + New to create a lost report",
    emptyStateFoundDesc = "Tap + New to create a found report",
    emptyStateContribDesc = "Your contributions will appear here",
    reportsCountLabel = "reports",
    myReportsHeader = "My Reports",
    btnNew = "New",

    // Profil
    userDataSection = "User Data",
    nameLabel = "Name",
    preferencesSection = "Preferences",
    darkModeLabel = "Dark Mode",
    languageLabel = "Language",
    langIndonesian = "Indonesian",
    langEnglish = "English",
    btnLogout = "Log Out",
    selectLanguageTitle = "Select Language",

    // Dialogs
    deleteReportTitle = "Delete Report?",
    deleteReportMessage = "Report \"%s\" will be permanently deleted and cannot be recovered.",
    isThisYoursTitle = "Is This Yours?",
    isThisYoursMessage = "Is this really your lost item? Please ensure the information is correct before confirming.",
    btnYesItsMine = "Yes, It's Mine",
    contactFinderTitle = "Contact Finder",
    contactFinderMessage = "Please meet the finder of this item to pick it up. Make sure to bring proof of ownership if needed.",
    btnGotIt = "Got It",
    dialogFoundTitle = "Confirm Discovery",
    dialogFoundText = "Are you sure you found this item? If so, please hand it over to the Common Room immediately.",
    btnYesIFoundIt = "Yes, I Found It",
    dialogMineTitle = "Pick Up Your Item",
    dialogMineText = "Please go to the Common Room to pick up your item by showing your identity.",
    btnUnderstood = "Understood"
)
