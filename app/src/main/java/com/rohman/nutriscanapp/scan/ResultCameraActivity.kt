package com.rohman.nutriscanapp.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.rohman.nutriscanapp.databinding.ActivityResultCameraBinding

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

        binding.btnBack.setOnClickListener { onBackPressed() }
        binding.btnDetail.setOnClickListener {
            val intent = Intent(this, DetailResultCameraActivity::class.java)
            intent.putExtra(DetailResultCameraActivity.EXTRA_IMAGE_URI, imageUri)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
    }
}
