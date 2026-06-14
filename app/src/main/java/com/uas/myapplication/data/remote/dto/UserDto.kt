    package com.uas.myapplication.data.remote.dto

    import com.uas.myapplication.domain.model.User

    data class UserDto(
        val uid: String = "",
        val nama_lengkap: String = "",
        val nim: String = "",
        val email: String = "",
        val nomor_whatsapp: String = "",
        val peran: String = "mahasiswa"
    ) {
        fun toDomain(): User = User(
            uid           = uid,
            namaLengkap   = nama_lengkap,
            nim           = nim,
            email         = email,
            nomorWhatsapp = nomor_whatsapp,
            peran         = peran
        )
    }

    fun User.toMap(): Map<String, Any> = mapOf(
        "uid"             to uid,
        "nama_lengkap"    to namaLengkap,
        "nim"             to nim,
        "email"           to email,
        "nomor_whatsapp"  to nomorWhatsapp,
        "peran"           to peran
    )