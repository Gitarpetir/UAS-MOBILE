package com.uas.myapplication.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uas.myapplication.data.local.dao.LaporanDao
import com.uas.myapplication.data.local.entity.LaporanEntity

@Database(entities = [LaporanEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun laporanDao(): LaporanDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cariin_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
