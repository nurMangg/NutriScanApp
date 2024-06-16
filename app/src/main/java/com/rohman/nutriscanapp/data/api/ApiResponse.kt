package com.rohman.nutriscanapp.data.api

data class ApiResponse(
    val status: Int,
    val message: String,
    val data: List<PredictionData>
)

data class PredictionData(
    val bahan: List<String>,
    val calory: String,
    val carbo: String,
    val desc: String,
    val lemak: String,
    val name: String,
    val protein: String,
    val confidence: String
)
