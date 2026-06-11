# PRD — Cari.in (Lost & Found Campus App)

> **Platform:** Android (Mobile-First)  
> **Arsitektur:** MVVM + Jetpack Compose  
> **Status:** In Development

---

## 1. Product Overview

**Cari.in** adalah aplikasi Android untuk melaporkan dan mencari **barang hilang/temuan** di lingkungan kampus. Aplikasi ini menggantikan sistem manual (grup WhatsApp) dengan katalog digital terpusat yang terstruktur, transparan, dan dapat diakses kapan saja.

**Masalah yang diselesaikan:**
- Informasi barang hilang tenggelam di grup chat
- Tidak ada sistem pengarsipan terpusat
- Proses klaim tidak transparan dan bergantung pada komunikasi manual

---

## 2. User Roles

| Role | Deskripsi | Hak Akses |
|---|---|---|
| **Pengguna Umum** | Mahasiswa, dosen, staf kampus | Lihat katalog, buat laporan, kelola laporan sendiri |
| **Admin** | Pengurus Himpunan Mahasiswa | Moderasi laporan, verifikasi klaim, hapus data tidak valid |

---

## 3. Tech Stack

| Kategori | Teknologi |
|---|---|
| Platform | Android (Kotlin + Jetpack Compose) |
| Arsitektur | MVVM (Model-View-ViewModel) |
| Database Lokal | Room Database |
| Auth | Firebase Authentication |
| Database Cloud | Firebase Firestore |
| Storage Foto | **Cloudinary** _(bukan Firebase Storage)_ |
| Image Loading | Coil (Coroutine Image Loader) |
| UI Tema | Dark Mode supported |
| Bahasa | Bahasa Indonesia + English support |

> ⚠️ **Catatan Penting:** Storage foto menggunakan **Cloudinary**, bukan Firebase Storage.

---

## 4. App Navigation & User Flow

```
App Launch
  └─ Onboarding/Dashboard (pengguna baru)
       └─ Login (Firebase Auth — Google / Email)
            └─ Home (Katalog Barang)
                 ├─ [FAB] Tombol "Lapor" → Form Pelaporan
                 ├─ [Tap Item] → Detail Barang → Profil Pelapor
                 └─ [Menu] Laporanku → Edit / Update Status / Hapus
```

**Alur Status Barang:**
```
Dalam Role Pengguna Umum
"Hilang" / "Ditemukan"  →  (diklaim)  →  "Sudah Diklaim"

Dalam Role Admin
Status Barang sudah ditemuka -> Ubah status barang menjadi "Selesai" 
```

---

## 5. Features & Functional Requirements

### F1 — Formulir Pelaporan ⭐ `Must-Have`
- Form untuk melaporkan barang **Hilang** atau **Ditemukan**
- Input: Nama Barang, Deskripsi, Kategori Status (Hilang/Ditemukan), Foto Barang
- Foto diunggah ke **Cloudinary**
- Validasi wajib: semua kolom harus terisi sebelum submit
- Tampilkan pesan error inline jika ada kolom kosong
- Notifikasi sukses setelah laporan terkirim → data muncul otomatis di Katalog
- Warna antarmuka berubah berdasarkan kategori laporan (Hilang vs Ditemukan) untuk mencegah kebingungan
- **MVVM wajib diimplementasikan** agar data form tidak hilang saat rotasi layar

### F2 — Katalog Barang ⭐ `Must-Have`
- Halaman utama aplikasi, memuat semua laporan barang (hilang & ditemukan)
- Setiap item menampilkan: thumbnail foto, nama barang, label status
- Mendukung filter/sort berdasarkan tanggal terbaru
- Tampilkan pesan informatif jika data kosong
- Gambar dimuat secara async menggunakan **Coil** (background thread + cache lokal)

### F3 — Detail Barang ⭐ `Must-Have`
- Dibuka saat pengguna mengetuk item di Katalog
- Tampilkan: foto ukuran penuh, deskripsi lengkap, waktu laporan, identitas pelapor
- Ikon visual untuk waktu (kalender) dan lokasi memudahkan scan informasi
- Tersedia akses ke Profil Pelapor untuk komunikasi awal

### F4 — Manajemen Laporan Saya ⭐ `Must-Have`
- Halaman "Laporanku" hanya menampilkan laporan milik pengguna yang login
- Fitur: Edit deskripsi, Update status (Hilang → Sudah Diklaim), Hapus laporan
- Tombol Edit dan Hapus dibedakan warnanya untuk mencegah salah klik
- Perubahan status langsung tercermin di Katalog secara real-time
- Data laporan disimpan di **Room Database** (lokal) + sinkronisasi ke **Firestore** (cloud)

### F5 — Dashboard / Onboarding `Should-Have`
- Ditampilkan saat pengguna baru pertama membuka aplikasi
- Berisi: ilustrasi + penjelasan singkat 3 nilai utama (Lapor Hilang, Telusuri Temuan, Bantu Sesama)
- Tombol "Lewati" untuk langsung ke fitur utama
- Tombol navigasi utama menuju fitur pelaporan dan katalog

### F6 — Profil Pengguna `Should-Have`
- Diakses dari halaman Detail Barang
- Menampilkan informasi kontak pelapor (nama, kontak)
- Tampilkan imbauan: serah terima barang dilakukan melalui Ruang Himpunan
- Tombol ubah/hapus **disembunyikan** jika pengguna yang melihat bukan pemilik laporan
- Login via Google didukung
- Warna merah pada tombol Logout untuk mencegah klik tidak sengaja

---

## 6. User Stories & Acceptance Criteria

| # | User Story | Acceptance Criteria |
|---|---|---|
| US-1 | Sebagai pengguna, saya ingin mengisi formulir laporan beserta foto, agar info barang diketahui pengguna lain. | Form tersedia, semua kolom wajib diisi, validasi error muncul jika kosong, notifikasi sukses muncul, data tampil di Katalog. |
| US-2 | Sebagai pengguna, saya ingin melihat daftar semua barang hilang/temuan di satu tempat. | Seluruh item tampil dalam list, setiap item ada foto pratinjau + nama + label status, pesan kosong muncul jika tidak ada data. |
| US-3 | Sebagai pencari, saya ingin menekan item di Katalog untuk melihat detail lengkap barang. | Tap item → buka halaman Detail, foto ukuran penuh ditampilkan, deskripsi + waktu + identitas pelapor termuat. |
| US-4 | Sebagai pelapor, saya ingin mengubah status barang dari "Hilang" menjadi "Sudah Diklaim". | Halaman "Laporanku" hanya berisi laporan milik sendiri, tombol update status tersedia, perubahan langsung tercermin di Katalog. |
| US-5 | Sebagai pengguna baru, saya ingin melihat halaman awal yang menjelaskan cara pakai aplikasi. | Layar Dashboard muncul saat pertama buka, ada ilustrasi + teks penjelasan, ada tombol navigasi ke fitur utama. |
| US-6 | Sebagai pengguna, saya ingin melihat info kontak pelapor untuk koordinasi serah terima barang. | Profil pelapor dapat diakses dari Detail Barang, info kontak jelas, imbauan serah terima melalui Himpunan tampil, tombol edit/hapus tersembunyi untuk bukan pemilik. |

---

## 7. Non-Functional Requirements

| Kategori | Requirement | Prioritas |
|---|---|---|
| **Usability** | Pengguna dapat menyelesaikan pelaporan < 10 menit tanpa tutorial eksternal | Must-Have |
| **Performance** | Katalog memuat data dengan cepat; foto dimuat async via Coil | Must-Have |
| **Reliability** | Data formulir tidak hilang saat rotasi layar (MVVM + state management) | Must-Have |
| **Security** | Info kontak pelapor hanya bisa diakses oleh pengguna kampus yang terautentikasi | Must-Have |
| **Usability** | Dark Mode dan dukungan Bahasa Inggris tersedia | Should-Have |

---

## 8. Dependencies

| Dependency | Kategori | Fungsi |
|---|---|---|
| **Coil** | Library UI | Async image loading dari URL ke tampilan Katalog, Form, Detail Barang |
| **Room Database** | Library Lokal | Penyimpanan lokal untuk riwayat "Laporanku" + operasi BREAD offline |
| **Firebase Auth** | External SDK | Login/registrasi pengguna (Google & Email) |
| **Firebase Firestore** | External SDK | Database cloud real-time untuk teks laporan → distribusi ke Katalog |
| **Cloudinary** | External API | Penyimpanan file foto barang (bukan Firebase Storage) |

---

## 9. Constraints & Assumptions

- Aplikasi **hanya untuk Android**
- Memerlukan koneksi internet untuk upload foto & sinkronisasi Firestore
- Room Database digunakan sebagai local cache sementara menunggu integrasi cloud penuh
- Sistem bergantung pada partisipasi jujur komunitas kampus (*crowdsourcing*)
- Admin (Pengurus Himpunan) diasumsikan kooperatif dan aktif memoderasi data
- Wi-Fi kampus diasumsikan memadai untuk upload foto real-time

---

## 10. Risks & Mitigations

| Risiko | Dampak | Mitigasi |
|---|---|---|
| Laporan palsu / spam | Tinggi | Wajib login sebelum akses Form; Admin bisa hapus postingan spam |
| Foto kartu identitas bocor | Tinggi | Teks peringatan di Form agar sensor data sensitif sebelum foto; akses Profil hanya untuk user terautentikasi |
| Performa buruk saat banyak foto | Sedang | Coil async + cache lokal; kompresi otomatis sebelum upload ke Cloudinary |
| Penumpukan laporan usang | Sedang | Filter/sort berdasarkan tanggal; arsip otomatis laporan lama |

---

## 11. Revision Notes _(Wajib Diperhatikan)_

> Ini adalah revisi dari PRD awal yang **harus diterapkan dalam development:**

1. **Storage foto:** Menggunakan **Cloudinary**, bukan Firebase Storage
2. **Fitur tambahan:** Dark Mode harus didukung
3. **Fitur tambahan:** Dukungan Bahasa Inggris (bilingual: ID + EN)
4. **Fitur tambahan:** Halaman Authentication untuk Role Admin
5. **Fitur tambahan:** Halaman Dasbor untuk Role Admin
6. **Fitur tambahan:** Halaman Katalog Barang untuk Role Admin berubah menjadi Halaman Kelola Laporan 
---

## 12. Design Reference

- Figma: [Lost & Found App Design](https://www.figma.com/design/wgLSqPSO1gnoMomI7LmJ9l/Lost---Found?node-id=0-1&p=f&t=AesAQe7wvxxkdZ5r-0)

---

_PRD ini disusun untuk keperluan pengembangan aplikasi Cari.in sebagai proyek UAS._  
_Authors: Muhammad Azriel Akbar (2410817110011) & Muhammad Alfi Gunawan (2410817110009)_