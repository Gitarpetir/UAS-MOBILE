package com.uas.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.uas.myapplication.data.local.entity.LaporanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LaporanDao {
    @Query("SELECT * FROM laporan_cache ORDER BY waktuDibuat DESC")
    fun getAllLaporan(): Flow<List<LaporanEntity>>

    @Query("SELECT * FROM laporan_cache WHERE id = :id")
    fun getLaporanById(id: String): LaporanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLaporan(laporan: LaporanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<LaporanEntity>)

    @Query("DELETE FROM laporan_cache")
    fun clearAll()

    @Query("DELETE FROM laporan_cache WHERE id = :id")
    fun deleteById(id: String)
}
