package com.rohman.nutriscanapp.scan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.data.api.PredictionData
import com.rohman.nutriscanapp.databinding.ActivityResultCameraBinding

class ResultCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultCameraBinding
    private var imageUri: String? = null
    private var predictResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUri = intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)
        predictResult = intent.getStringExtra(EXTRA_PREDICT_RESULT)

        imageUri?.let {
            val uri = Uri.parse(it)
            Glide.with(this).load(uri).into(binding.resultImage)
        }

        predictResult?.let { result ->
            val listType = object : TypeToken<List<PredictionData>>() {}.type
            val predictionList: List<PredictionData> = Gson().fromJson(result, listType)

            val message = StringBuilder()
            var showDetailButton = false

            predictionList.forEach { prediction ->
                val confidence = prediction.confidence.removeSuffix("%").toFloatOrNull() ?: 0f
                if (confidence < 55.0) {
                    binding.resultTextView.text = getString(R.string.confidence_kurang)
                } else {
                    binding.resultTextView.text = capitalizeEveryWord(prediction.name)
                    showDetailButton = true
                }
            }

            moveToDetail(showDetailButton)
        }

        binding.btnBack.setOnClickListener { onBackPressed() }
        playAnimation()
    }

    private fun moveToDetail(showDetail: Boolean) {
        if (!showDetail) {
            binding.btnDetail.visibility = View.GONE
            binding.resultTextView.setPadding(
                binding.resultTextView.paddingStart,
                binding.resultTextView.paddingTop,
                0,
                binding.resultTextView.paddingBottom
            )
        } else {
            binding.btnDetail.setOnClickListener {
                val intent = Intent(this, DetailResultCameraActivity::class.java)
                intent.putExtra(DetailResultCameraActivity.EXTRA_IMAGE_URI, imageUri)
                intent.putExtra(EXTRA_PREDICT_RESULT, predictResult)
                startActivity(
                    intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
                )
            }
        }
    }

    private fun capitalizeEveryWord(input: String): String {
        return input.split(" ").joinToString(" ") { it.capitalize() }
    }

    private fun playAnimation() {
        val imageResult = ObjectAnimator.ofFloat(binding.resultImage, View.ALPHA, 0f, 1f).setDuration(200)
        val clHead = ObjectAnimator.ofFloat(binding.clHead, View.ALPHA, 0f, 1f).setDuration(200)
        val clResult = ObjectAnimator.ofFloat(binding.clResult, View.ALPHA, 0f, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                imageResult,
                clHead,
                clResult
            )
            startDelay = 100
        }.start()
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_PREDICT_RESULT = "extra_predict_result"
    }
}
