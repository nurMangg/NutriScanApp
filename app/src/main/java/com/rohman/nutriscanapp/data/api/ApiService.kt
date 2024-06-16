package com.rohman.nutriscanapp.data.api

import com.rohman.nutriscanapp.daftarmakanan.data.MakananResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/predict")
    fun predict(@Body requestBody: RequestBody): Call<ApiResponse>

    @GET("/list")
    fun getMakanan(): Call<MakananResponse>
}