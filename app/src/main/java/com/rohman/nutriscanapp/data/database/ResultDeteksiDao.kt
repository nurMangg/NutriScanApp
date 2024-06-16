package com.rohman.nutriscanapp.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ResultDeteksiDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(resultDeteksi: ResultDeteksi)

    @Update
    fun update(resultDeteksi: ResultDeteksi)

    @Delete
    fun delete(resultDeteksi: ResultDeteksi)

    @Query("SELECT * from result_deteksi ORDER BY id ASC")
    fun gettAllData(): LiveData<List<ResultDeteksi>>

    @Query("DELETE FROM result_deteksi WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("DELETE FROM result_deteksi WHERE date = (SELECT MAX(date) FROM result_deteksi)")
    suspend fun deleteLatestEntry()

    @Query("SELECT * FROM result_deteksi WHERE DATE(date) = :date ORDER BY id ASC")
    fun getDataByDate(date: String): LiveData<List<ResultDeteksi>>

}