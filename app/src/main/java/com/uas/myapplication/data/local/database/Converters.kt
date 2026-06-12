package com.uas.myapplication.data.local.database

import androidx.room.TypeConverter
import com.uas.myapplication.domain.model.JenisLaporan
import com.uas.myapplication.domain.model.StatusBarang

class Converters {
    @TypeConverter
    fun fromStatusBarang(value: StatusBarang): String {
        return value.name
    }

    @TypeConverter
    fun toStatusBarang(value: String): StatusBarang {
        return enumValueOf<StatusBarang>(value)
    }

    @TypeConverter
    fun fromJenisLaporan(value: JenisLaporan): String {
        return value.name
    }

    @TypeConverter
    fun toJenisLaporan(value: String): JenisLaporan {
        return enumValueOf<JenisLaporan>(value)
    }
}
