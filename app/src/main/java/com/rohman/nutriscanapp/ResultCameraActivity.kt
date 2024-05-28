package com.rohman.nutriscanapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rohman.nutriscanapp.databinding.ActivityDashboardBinding
import com.rohman.nutriscanapp.databinding.ActivityResultCameraBinding

class ResultCameraActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResultCameraBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityResultCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener{ onBackPressed() }
    }
}