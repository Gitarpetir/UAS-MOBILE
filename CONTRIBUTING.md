# Panduan Kontribusi Anggota Tim Cari.in

Dokumen ini berisi konvensi yang **wajib diikuti** oleh semua anggota tim selama pengerjaan proyek.

---

## Alur Kerja

Setiap mengerjakan fitur, ikuti alur berikut:

```
Ambil issue yang di-assign → Buat branch baru → Kerjakan fitur
        → Push branch → Buat Pull Request → Minta review
        → Di-approve → Merge ke main → Hapus branch
```

Wajib jalankan perintah ini **setiap kali mulai kerja** agar kode selalu up to date:

```bash
git checkout main
git pull origin main
git checkout nama-branch-kamu
git merge main
```

---

## Pembagian Area Kerja

Untuk menghindari konflik **koordinasi dulu di grup WhatsApp** sebelum mulai.

## Konvensi Penamaan Branch

Format:

```
<prefix>/<nomor-issue>-<deskripsi-singkat>
```

Prefix yang dipakai:

| Prefix     | Dipakai untuk                    |
| ---------- | -------------------------------- |
| `feat`     | Fitur baru                       |
| `fix`      | Perbaikan bug                    |
| `ui`       | Perubahan tampilan murni         |
| `conf`     | Konfigurasi, setup, dependencies |
| `docs`     | Dokumentasi                      |
| `refactor` | Rapikan kode tanpa ubah fungsi   |

---

Contoh yang **benar**:

```bash
git checkout -b feat/12-form-halaman-laporan
git checkout -b ui/15-redesign-card-katalog
git checkout -b fix/24-status-tidak-update-setelah-approve
git checkout -b conf/03-setup-cloudinary
git checkout -b docs/01-tambah-readme
```

## Konvensi Commit Message

Format:

```
<prefix>: <deskripsi singkat dalam bahasa indonesia>
```

Aturan penulisan:

- Huruf kecil semua
- Tanpa titik di akhir kalimat
- Maksimal 72 karakter
- Gunakan kalimat perintah

Contoh yang **benar**:

```bash
git commit -m "feat: tambah card barang di halaman katalog"
git commit -m "fix: perbaiki status badge tidak update setelah konfirmasi"
git commit -m "ui: redesign card katalog barang"
git commit -m "conf: integrasi firebase authentication"
git commit -m "docs: tambah panduan instalasi di README"
git commit -m "refactor: sederhanakan logika laporanku"
```

Contoh yang **salah**:

```bash
# Huruf kapital
git commit -m "Feat: Tambah Form nama pada login"

# Pakai titik di akhir
git commit -m "feat: tambah form nama pada login."

# Terlalu panjang
git commit -m "feat: tambah button ditemukannya baranag yang memiliki input tanggal pinjam tanggal kembali dan alasan peminjaman"

# Tidak jelas
git commit -m "update"
git commit -m "fix bug"
git commit -m "perubahan"
```

Kalau commit terkait issue tertentu, sebut nomornya di body commit:

```bash
git commit -m "feat: tambah form pengajuan peminjaman barang

Closes #12"
```

---

## Konvensi Issue

Format judul issue:

```
<nomor urut>. [<prefix>] <deskripsi lengkap>
```

Contoh:

```
12. [feat] Buat form login
15. [ui] Redesign card katalog barang
24. [fix] Testing manual semua alur dan perbaikan bug
```

Prefix issue:

| Prefix | Dipakai untuk         |
| ------ | --------------------- |
| `feat` | Fitur baru            |
| `fix`  | Bug atau perbaikan    |
| `ui`   | Perubahan tampilan    |
| `conf` | Konfigurasi dan setup |
| `docs` | Dokumentasi           |

---

## Konvensi Pull Request

Format judul PR:

```
<prefix>: <deskripsi singkat> (#<nomor issue>)
```

Contoh:

```
feat: tambah form pengajuan peminjaman (#12)
ui: redesign card katalog barang (#15)
fix: perbaiki status tidak update setelah approve (#24)
```

Wajib isi semua bagian di template deskripsi PR yang sudah tersedia.

---

## File yang Tidak Boleh Diedit Bersamaan

File-file berikut rawan konflik jika diedit bersamaan oleh lebih dari satu orang:

| File                     | Aturan                                                         |
| ------------------------ | -------------------------------------------------------------- |
| `app/Navigation.kt`      | Koordinasi dulu di grup WhatsApp sebelum edit                  |
| `app/ui/theme/Theme.kt`  | Sepakati design token di awal, jangan diubah sembarangan       |
| `app/ui/theme/Color.kt`  | Sama seperti Theme.kt, koordinasi sebelum mengubah             |
| `google-services.json`   | **Jangan di-commit ke repository**, sudah masuk `.gitignore`   |
| `local.properties`       | Jangan di-commit, berisi path SDK lokal masing-masing komputer |
| `build.gradle (Project)` | Koordinasi jika perlu tambah plugin atau dependency baru       |

---

## Aturan Tambahan

- Jangan push langsung ke `main` — selalu lewat Pull Request
- Jangan merge PR milik sendiri — minta anggota lain untuk review
- Selesaikan semua konflik sebelum minta review
- Hapus branch setelah PR berhasil di-merge
- Jangan commit file `google-services.json`, `local.properties`, atau folder `build/`
- Pastikan aplikasi bisa di-build tanpa error sebelum membuat Pull Request
