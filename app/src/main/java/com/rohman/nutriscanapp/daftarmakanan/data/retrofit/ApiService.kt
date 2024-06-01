package com.rohman.nutriscanapp.daftarmakanan.data.retrofit

import com.rohman.nutriscanapp.daftarmakanan.data.MakananResponseItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("api/makanan")
    fun getMakanan(): Call<List<MakananResponseItem>>
}