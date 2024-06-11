package com.rohman.nutriscanapp.riwayat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rohman.nutriscanapp.data.repository.ResultDeteksiRepository

class RiwayatViewModelFactory(
    private val repository: ResultDeteksiRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RiwayatViewModel::class.java)) {
            return RiwayatViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}