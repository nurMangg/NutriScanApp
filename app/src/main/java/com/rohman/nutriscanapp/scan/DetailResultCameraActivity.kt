package com.rohman.nutriscanapp.scan

import BahanBahanAdapter
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.util.splitToIntList
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.data.api.PredictionData
import com.rohman.nutriscanapp.data.database.ResultDeteksi
import com.rohman.nutriscanapp.data.database.ResultDeteksiRoomDatabase
import com.rohman.nutriscanapp.data.repository.ResultDeteksiRepository
import com.rohman.nutriscanapp.databinding.ActivityDetailResultCameraBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DetailResultCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailResultCameraBinding
    private var initialHeight = 0
    private var isBeingDragged = false
    private var initialTouchY = 0f

    private var isSaved = false
    private var savedId: Long? = null


    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    @SuppressLint("NewApi")
    private val current = LocalDateTime.now().format(formatter)

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(ResultCameraActivity.EXTRA_IMAGE_URI)
        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            Glide.with(this).load(uri).into(binding.detailResultImg)
        }

        val predictResult = intent.getStringExtra(EXTRA_PREDICT_RESULT)
        if (predictResult != null) {
            val listType = object : TypeToken<List<PredictionData>>() {}.type
            val predictionList: List<PredictionData> = Gson().fromJson(predictResult, listType)

            if (predictionList.isNotEmpty()) {
                val resultprediction = predictionList[0]
                displayPredictions(resultprediction)
                setupIngredientRecyclerView(resultprediction.bahan)
                binding.btnSave.setOnClickListener {
                    lifecycleScope.launch(Dispatchers.IO) {
                        if (isSaved) {
                            deleteFromDatabase()
                        } else {
                            saveToDatabase(resultprediction)
                        }
                        runOnUiThread {
                            toggleSaveButton()
                        }
                    }
                }
            }
        }

        binding.closeImage.setOnClickListener { onBackPressed() }

        binding.scrollView.post {
            initialHeight = binding.scrollView.height
        }

        binding.scrollView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialTouchY = event.rawY
                    isBeingDragged = true
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (isBeingDragged) {
                        val deltaY = (initialTouchY - event.rawY).toInt()
                        adjustLayoutHeight(deltaY)
                        initialTouchY = event.rawY
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isBeingDragged = false
                    true
                }
                else -> false
            }
        }
    }

    private fun toggleSaveButton() {
        if (isSaved) {
            binding.btnSave.setImageResource(R.drawable.baseline_bookmark_add_24)
        } else {
            binding.btnSave.setImageResource(R.drawable.baseline_bookmark_add_before_24)
        }
    }
    private fun setupIngredientRecyclerView(ingredients: List<String>) {
        binding.recycleBahan.layoutManager = LinearLayoutManager(this)
        binding.recycleBahan.adapter = BahanBahanAdapter(ingredients)
    }

    private fun displayPredictions(prediction: PredictionData) {
        binding.contentTitle.text = prediction.name
        binding.contentDesc.text = prediction.desc
        binding.nilaiKarbo.text = prediction.carbo
        binding.nilaiProtein.text = prediction.protein
        binding.nilaiKalori.text = prediction.calory
        binding.nilaiLemak.text = prediction.lemak
    }

    private suspend fun saveToDatabase(prediction: PredictionData) {
        try {
            val resultDeteksiDao = ResultDeteksiRoomDatabase.getDatabase(application).resultDatabaseDao()
            val resultDeteksiRepository = ResultDeteksiRepository(resultDeteksiDao)
            val resultDeteksi = ResultDeteksi(
                name = prediction.name,
                desc = prediction.desc,
                carbo = prediction.carbo,
                protein = prediction.protein,
                calory = prediction.calory,
                lemak = prediction.lemak,
                bahan = prediction.bahan,
                tanggal = current
            )
            resultDeteksiRepository.insert(resultDeteksi)
            isSaved = true
        } catch (e: Exception) {
            Log.e("SaveToDatabase", "Error saving data: ${e.message}")
        }
    }

    private suspend fun deleteFromDatabase() {
        try {
            val resultDeteksiDao = ResultDeteksiRoomDatabase.getDatabase(application).resultDatabaseDao()
            resultDeteksiDao.deleteLatestEntry()
            isSaved = false
        } catch (e: Exception) {
            Log.e("DeleteFromDatabase", "Error deleting data: ${e.message}")
        }
    }

    private fun adjustLayoutHeight(deltaY: Int) {
        val newHeight = (binding.scrollView.height + deltaY).coerceIn(initialHeight, initialHeight + 750)
        val layoutParams = binding.scrollView.layoutParams
        layoutParams.height = newHeight
        binding.scrollView.layoutParams = layoutParams

        Log.d("DetailResultCameraActivity", "Adjusting Height: $newHeight")
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICT_RESULT = "extra_predict_result"
    }
}