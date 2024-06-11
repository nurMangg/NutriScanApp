package com.rohman.nutriscanapp.riwayat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohman.nutriscanapp.data.database.ResultDeteksi
import com.rohman.nutriscanapp.data.repository.ResultDeteksiRepository
import kotlinx.coroutines.launch

class RiwayatViewModel(private val repository: ResultDeteksiRepository) : ViewModel() {

    val allData: LiveData<List<ResultDeteksi>> = repository.allResultDeteksi

    fun getDataByDate(date: String): LiveData<List<ResultDeteksi>> {
        return repository.getDataByDate(date)
    }

    private var _filteredData = MutableLiveData<List<ResultDeteksi>?>()
    val filteredData: MutableLiveData<List<ResultDeteksi>?> get() = _filteredData

    fun filterDataByDate(date: String) {
        viewModelScope.launch {
            val data = repository.getDataByDate(date).value
            _filteredData.postValue(data)
        }
    }
}