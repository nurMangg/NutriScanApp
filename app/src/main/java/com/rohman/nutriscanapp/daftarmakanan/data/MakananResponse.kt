package com.rohman.nutriscanapp.daftarmakanan.data

import com.google.gson.annotations.SerializedName

data class MakananResponse(
    @field:SerializedName("data")
    val data: List<MakananResponseItem>,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: Int
)

data class MakananResponseItem(
    @field:SerializedName("carbo")
    val carbohydrates: Int,

    @field:SerializedName("protein")
    val protein: Int,

    @field:SerializedName("lemak")
    val fat: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("desc")
    val description: String,

    @field:SerializedName("bahan")
    val ingredients: List<String>,

    @field:SerializedName("calory")
    val calories: Int
)
