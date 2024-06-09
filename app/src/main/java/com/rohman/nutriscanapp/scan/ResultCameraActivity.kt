package com.rohman.nutriscanapp.scan

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rohman.nutriscanapp.data.api.ApiConfig
import com.rohman.nutriscanapp.data.api.ApiResponse
import com.rohman.nutriscanapp.data.api.PredictionData
import com.rohman.nutriscanapp.databinding.ActivityResultCameraBinding
import com.rohman.nutriscanapp.scan.CameraActivity
import com.rohman.nutriscanapp.scan.DetailResultCameraActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream

class ResultCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
        if (imageUri != null) {
            val uri = Uri.parse(imageUri)
            Glide.with(this).load(uri).into(binding.resultImage)
        }

        val predictResult = intent.getStringExtra(EXTRA_PREDUCT_RESULT)
        if (predictResult != null) {
            binding.resultTextView.text = predictResult
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnDetail.setOnClickListener {
            val intent = Intent(this, DetailResultCameraActivity::class.java)
            intent.putExtra(DetailResultCameraActivity.EXTRA_IMAGE_URI, imageUri)
            startActivity(intent)
        }
    }



//    private fun showLoading(isLoading: Boolean) {
//        // Implement your loading UI logic here, such as showing or hiding a progress bar
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//    }



    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDUCT_RESULT = "extra_predict_result"
    }
}
