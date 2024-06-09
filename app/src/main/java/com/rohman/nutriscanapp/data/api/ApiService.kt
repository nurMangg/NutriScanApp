package com.rohman.nutriscanapp.data.api

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("/predict")
    fun predict(@Body requestBody: RequestBody): Call<ApiResponse>
}