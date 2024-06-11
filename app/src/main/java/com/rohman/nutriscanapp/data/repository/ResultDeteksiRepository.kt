package com.rohman.nutriscanapp.data.repository

import androidx.lifecycle.LiveData
import com.rohman.nutriscanapp.data.database.ResultDeteksi
import com.rohman.nutriscanapp.data.database.ResultDeteksiDao

class ResultDeteksiRepository(private val resultDeteksiDao: ResultDeteksiDao) {
    suspend fun insert(resultDeteksi: ResultDeteksi) {
        resultDeteksiDao.insert(resultDeteksi)
    }

    suspend fun delete(date: String) {
        resultDeteksiDao.deleteByDate(date)
    }

    val allResultDeteksi: LiveData<List<ResultDeteksi>> = resultDeteksiDao.gettAllData()

    fun getDataByDate(date: String): LiveData<List<ResultDeteksi>> {
        return resultDeteksiDao.getDataByDate(date)
    }
}
