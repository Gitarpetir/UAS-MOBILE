# Cari.in - Lost & Found Campus App

Cari.in adalah aplikasi Android *Mobile-First* yang dirancang untuk mempermudah civitas akademika kampus dalam melaporkan, mencari, dan melacak barang hilang maupun barang temuan. Aplikasi ini dibangun dengan arsitektur **MVVM (Model-View-ViewModel)** dan UI modern menggunakan **Jetpack Compose**.

---

## Cara Menjalankan Aplikasi

1. **Clone Repository**
   ```bash
   git clone https://github.com/username/Cari.in.git
   cd Cari.in
   ```
2. **Buka dengan Android Studio**
   Buka *project* menggunakan Android Studio versi terbaru yang mendukung *Jetpack Compose* dan Gradle KTS.
3. **Konfigurasi API Keys**
   *(Dapatkan API Key Cuaca di situs OpenWeatherMap).*
4. **Sync Project & Run**
   Lakukan *Sync Project with Gradle Files*, pilih emulator atau *device* fisik, lalu jalankan.

---

## Informasi API yang Digunakan

Aplikasi ini mengandalkan beberapa API dan layanan pihak ketiga:
1. **Firebase Authentication:** Untuk autentikasi pengguna.
2. **Firebase Firestore:** *Database cloud* utama yang digunakan untuk menyimpan teks dan status laporan pengguna secara *real-time*.
3. **Cloudinary API:** Layanan *image hosting* eksternal. Semua foto laporan barang yang diunggah akan masuk ke Cloudinary.
4. **OpenWeatherMap API:** Digunakan untuk menampilkan informasi cuaca *real-time*.

---

##  Penjelasan Fitur

### Fitur Wajib (Core Features)
* **Autentikasi :** Registrasi dan login pengguna menggunakan Firebase Auth.
* **Katalog Barang (Browse & Read):** Menelusuri seluruh laporan barang hilang/temuan dalam *list* yang dinamis, beserta detail lengkap barang.
* **Form Pelaporan (Add):** Form pembuatan laporan dengan validasi dan *upload* gambar ke Cloudinary.
* **Manajemen Laporanku (Edit & Delete):** Mengubah detail/status laporan, dan menghapus laporan lama.
* **Local Database (Room):** Menyimpan *cache* riwayat "Laporanku" secara luring/lokal.
* **Role Admin:** Hak akses khusus untuk memoderasi laporan dan mengubah status menjadi "Selesai".

### Fitur Tambahan (Nilai Plus)
* **Real-time Data Sync:** Katalog selalu *up-to-date* otomatis tanpa *pull-to-refresh*.
* **Cuaca Real-time:** Menampilkan informasi cuaca menggunakan API OpenWeatherMap.
* **Manual Dependency Injection:** Pemisahan *Data Layer* dan *UI Layer* menggunakan arsitektur DI.
* **Preferensi Tema (Dark Mode):** Dukungan Mode Gelap yang bisa diganti melalui Profil.
* **Dukungan Multibahasa (Bilingual):**  Bahasa pengantar aplikasi bisa dipilih antara *Bahasa Indonesia* dan *English*.

---

## Struktur Folder

Kode sumber disusun menggunakan prinsip *Clean Architecture* dan *MVVM*:
```text
app/src/main/java/com/uas/myapplication/
│
├── data/              # Data Layer (sumber kebenaran data)
│   ├── local/         # Konfigurasi Room Database & DataStore Preferences
│   ├── remote/        # Remote Data Source (API Cuaca, Cloudinary, Firebase)
│   ├── repository/    # Implementasi Repository
│   └── remote/dto/    # Data Transfer Objects untuk parsing JSON
│
├── di/                # Dependency Injection (Manual DI Container)
│
├── domain/            # Domain Layer (Model/Entity Utama)
│
└── presentation/      # UI Layer (Jetpack Compose)
    ├── auth/          # Layar Login/Register
    ├── home/          # Katalog Barang Utama & Widget Cuaca
    ├── laporan/       # Form Buat Laporan & Detail Laporan
    └── profil/        # Pengaturan Akun, Bahasa, dan Tema
```

---
*Dibuat oleh: Muhammad Azriel Akbar & Muhammad Alfi Gunawan*
