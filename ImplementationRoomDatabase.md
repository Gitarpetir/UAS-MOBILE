# Implementasi Room Database — Cari.in App

## Latar Belakang
Aplikasi **Cari.in** saat ini mengandalkan **Firebase Firestore** sebagai satu-satunya sumber data untuk daftar laporan (Dashboard, Katalog, Laporanku). Ketentuan UAS mata kuliah Pemrograman Mobile mensyaratkan **penggunaan local database (Room)** sebagai bagian dari arsitektur aplikasi.

**Namun**, saat ini aplikasi **tidak memiliki mekanisme caching offline**. Jika pengguna membuka aplikasi tanpa koneksi internet, daftar laporan tidak dapat ditampilkan sama sekali karena bergantung penuh pada `snapshotListener`/`get()` Firestore secara realtime.

---

## Pendekatan: Room sebagai *Local Cache Layer* (Offline-First Read)

Karena aplikasi ini sudah memiliki arsitektur **Repository Pattern + Coroutines/Flow** untuk komunikasi dengan Firestore, pendekatan terbaik adalah menambahkan **Room Database sebagai layer cache** di antara Firestore dan ViewModel — bukan menggantikan Firestore sebagai source of truth.

> [!IMPORTANT]
> Pendekatan ini **bukan** migrasi penuh ke local-first database. Alasannya:
> 1. Firestore tetap menjadi *single source of truth* (realtime sync antar device/admin)
> 2. Cloudinary tetap menjadi penyimpanan file foto — Room hanya menyimpan **string URL**
> 3. Room hanya menyimpan field **teks ringan**; data sensitif (`noWhatsapp`) **tidak di-cache**
> 4. Tujuannya murni untuk **memenuhi requirement UAS** + memberi fallback saat offline

### Arsitektur:
LaporanEntity.kt (Room @Entity)

├── Menyimpan seluruh field teks laporan + fotoUrl (string)

└── TIDAK menyimpan noWhatsapp (privasi, hanya untuk Admin)LaporanDao.kt (Room @Dao)

├── getAllLaporan(): Flow<List<LaporanEntity>>

├── insertAll() / clearAll()

└── deleteById()AppDatabase.kt (RoomDatabase, singleton)

└── abstract fun laporanDao(): LaporanDaoLaporanRepository.kt

├── Cek koneksi internet (NetworkHelper)

├── Online  → ambil dari Firestore (Flow) → update cache Room → emit

├── Offline → ambil langsung dari Room → emit

└── submitLaporan(): Cloudinary upload → Firestore save → Room cache

---

---

## Proposed Changes

### Komponen Baru — Room Database Layer

#### [NEW] [LaporanEntity.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/data/local/entity/LaporanEntity.kt)

`@Entity` dengan `tableName = "laporan_cache"`. Berisi field:
- `id` (`@PrimaryKey`, samakan dengan document ID Firestore)
- `namaBarang`, `deskripsi`, `lokasi`, `tanggal`, `status`, `namaPelapor`
- `fotoUrl: String?` — hasil URL dari Cloudinary
- `lastUpdated: Long`

> **Catatan privasi**: field `noWhatsapp` **TIDAK** dimasukkan ke entity ini.

---

#### [NEW] [LaporanDao.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/data/local/dao/LaporanDao.kt)

`@Dao` interface berisi:
- `getAllLaporan(): Flow<List<LaporanEntity>>`
- `getLaporanById(id: String): LaporanEntity?`
- `insertLaporan(laporan: LaporanEntity)`
- `insertAll(list: List<LaporanEntity>)`
- `clearAll()`
- `deleteById(id: String)`

---

#### [NEW] [AppDatabase.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/data/local/database/AppDatabase.kt)

`@Database(entities = [LaporanEntity::class], version = 1)` — singleton via `getInstance(context)`, exposes `laporanDao()`.

---

#### [NEW] [NetworkHelper.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/data/util/NetworkHelper.kt)

Object utility — `isConnected(context: Context): Boolean` menggunakan `ConnectivityManager` + `NetworkCapabilities.NET_CAPABILITY_INTERNET`.

---

### Repository Updates — Mengintegrasikan Room dengan Firestore & Cloudinary

#### [MODIFY] [LaporanRepository.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/data/repository/LaporanRepository.kt)

1. Tambah parameter `laporanDao: LaporanDao` di constructor
2. **Fungsi `submitLaporan(...)`** (upload laporan baru):
    - Step 1: Upload foto ke Cloudinary via OkHttp → dapat `fotoUrl`
    - Step 2: Simpan data teks + `fotoUrl` + `noWhatsapp` ke Firestore
    - Step 3: Cache data (tanpa `noWhatsapp`) ke Room via `laporanDao.insertLaporan()`
3. **Fungsi `getLaporanList(context: Context): Flow<List<LaporanEntity>>`**:
    - Cek `NetworkHelper.isConnected(context)`
    - **Jika online**: subscribe Firestore realtime (`callbackFlow` + `addSnapshotListener`) → mapping ke `LaporanEntity` → `laporanDao.clearAll()` + `insertAll()` → emit list
    - **Jika offline**: `laporanDao.getAllLaporan()` → emit langsung dari Room

---

### Screen Updates — Mengintegrasikan Status Offline

Setiap screen daftar laporan (Dashboard, Katalog, Laporanku) akan dimodifikasi untuk:
1. Menggunakan `LaporanRepository.getLaporanList(context)` (bukan langsung Firestore)
2. Mengamati state `isOfflineMode: StateFlow<Boolean>` dari ViewModel
3. Menampilkan banner/indikator "Menampilkan data tersimpan terakhir" saat offline

---

#### Dashboard
#### [MODIFY] [DashboardViewModel.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/dashboard/DashboardViewModel.kt)
- Inject `laporanDao` via `AppDatabase.getInstance(context).laporanDao()`
- Ganti sumber `laporanList` dari Firestore langsung → `repository.getLaporanList(context)`
- Tambah `StateFlow<Boolean> isOfflineMode`

#### [MODIFY] [DashboardScreen.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/dashboard/DashboardScreen.kt)
- Tampilkan banner offline jika `isOfflineMode == true`
- "Barang Terbaru" tetap menggunakan data dari `LaporanEntity` (mapping field sama)

---

#### Katalog
#### [MODIFY] [KatalogViewModel.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/katalog/KatalogViewModel.kt)
- Sama seperti Dashboard: gunakan `repository.getLaporanList(context)` + `isOfflineMode`

#### [MODIFY] [KatalogScreen.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/katalog/KatalogScreen.kt)
- Tambah banner offline di atas list
- Filter chip (Semua/Hilang/Ditemukan) tetap berjalan di atas data `LaporanEntity` (filter di memory)

---

#### Laporanku
#### [MODIFY] [LaporankuViewModel.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/laporanku/LaporankuViewModel.kt)
- Konsumsi `repository.getLaporanList(context)`, filter berdasarkan `userId`/`namaPelapor` untuk tab Barangku/Temuanku/Kontribusi

#### [MODIFY] [LaporankuScreen.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/laporanku/LaporankuScreen.kt)
- Tambah banner offline

---

#### Form Laporan
#### [MODIFY] [BuatLaporanViewModel.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/laporan/BuatLaporanViewModel.kt)
- Panggil `repository.submitLaporan(...)` (Cloudinary → Firestore → Room cache) saat submit berhasil

---

### Shared Components

#### [NEW] [OfflineBanner.kt](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/src/main/java/com/uas/myapplication/presentation/ui/components/OfflineBanner.kt)
- Composable banner kecil: "Menampilkan data tersimpan terakhir" — dipanggil di Dashboard, Katalog, Laporanku saat `isOfflineMode == true`

#### [MODIFY] Kartu/Card laporan (semua file `*Card.kt` di `dashboard/components/`, `katalog/components/`, `ui/components/`)
- Pastikan `Coil AsyncImage` membaca `fotoUrl` dari `LaporanEntity` (string sama seperti dari Firestore)
- Set `placeholder` & `error` drawable untuk kondisi offline (foto tidak bisa di-load)

---

### Dependency & Konfigurasi

#### [MODIFY] [build.gradle.kts (Module: app)](file:///C:/Users/Zz/.gemini/antigravity/worktrees/UAS-MOBILE/add-room-database/app/build.gradle.kts)
```kotlin
dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}
```

---

## Verification Plan

### Manual Verification
1. Build project menggunakan Gradle — pastikan tidak ada compile error
2. Buat laporan baru (online) → cek data tersimpan di Firestore **dan** Room (gunakan App Inspection → Database)
3. Cek field `noWhatsapp` **tidak** muncul di tabel `laporan_cache` Room
4. Matikan Wi-Fi/data seluler → buka Dashboard/Katalog/Laporanku → pastikan data lama tetap tampil dari Room + banner offline muncul
5. Cek foto pada card: saat offline, Coil menampilkan placeholder (bukan crash)
6. Nyalakan kembali internet → buka ulang screen → data Firestore terbaru menggantikan cache Room

### Build Test
```bash
./gradlew assembleDebug
```