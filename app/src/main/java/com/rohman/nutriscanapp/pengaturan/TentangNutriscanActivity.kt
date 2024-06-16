package com.rohman.nutriscanapp.pengaturan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rohman.nutriscanapp.databinding.ActivityTentangNutriscanBinding

class TentangNutriscanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTentangNutriscanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangNutriscanBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}