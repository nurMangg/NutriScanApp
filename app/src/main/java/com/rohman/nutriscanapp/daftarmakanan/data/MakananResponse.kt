package com.rohman.nutriscanapp.daftarmakanan.data

import com.google.gson.annotations.SerializedName

data class MakananResponseItem(

	@field:SerializedName("carbohydrates")
	val carbohydrates: Int,

	@field:SerializedName("protein")
	val protein: Int,

	@field:SerializedName("fat")
	val fat: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("ingredients")
	val ingredients: List<String>,

	@field:SerializedName("calories")
	val calories: Int
)
