package com.rohman.nutriscanapp.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "result_deteksi")
@Parcelize
data class ResultDeteksi(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "desc")
    var desc: String? = null,

    @ColumnInfo(name = "carbo")
    var carbo: String? = null,

    @ColumnInfo(name = "protein")
    var protein: String? = null,

    @ColumnInfo(name = "calory")
    var calory: String? = null,

    @ColumnInfo(name = "lemak")
    var lemak: String? = null,

    @ColumnInfo(name = "bahan")
    var bahan: List<String>? = null,

    @ColumnInfo(name = "date")
    var tanggal: String? = null,

    @ColumnInfo(name = "image")
    val image: ByteArray?
) : Parcelable