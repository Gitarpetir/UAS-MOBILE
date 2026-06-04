    package com.uas.myapplication.data.remote.dto

    import com.uas.myapplication.domain.model.User

    /**
     * DTO (Data Transfer Object) untuk koleksi `users` di Firestore.
     * Semua field harus punya nilai default agar Firestore bisa
     * melakukan deserialisasi otomatis tanpa error.
     */
    data class UserDto(
        val uid: String = "",
        val nama_lengkap: String = "",
        val nim: String = "",
        val email: String = "",
        val nomor_whatsapp: String = "",
        val peran: String = "mahasiswa"
    ) {
        /**
         * Mengubah DTO menjadi Domain Model (User).
         * Dipanggil di Repository sebelum data dikirim ke Presentation Layer.
         */
        fun toDomain(): User = User(
            uid           = uid,
            namaLengkap   = nama_lengkap,
            nim           = nim,
            email         = email,
            nomorWhatsapp = nomor_whatsapp,
            peran         = peran
        )
    }

    /**
     * Extension function untuk mengubah Domain Model menjadi Map.
     * Map digunakan saat menyimpan/update data ke Firestore.
     */
    fun User.toMap(): Map<String, Any> = mapOf(
        "uid"             to uid,
        "nama_lengkap"    to namaLengkap,
        "nim"             to nim,
        "email"           to email,
        "nomor_whatsapp"  to nomorWhatsapp,
        "peran"           to peran
    )