package com.rohman.nutriscanapp

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rohman.nutriscanapp.databinding.ActivityDashboardBinding
import com.rohman.nutriscanapp.databinding.ActivityMainBinding
import java.util.Calendar

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val greetingTextView: TextView = findViewById(R.id.greetingTextView)
        val weatherIcon: ImageView = findViewById(R.id.weatherIcon)

        val (greetingMessage, iconResId) = getGreetingAndIcon()
        greetingTextView.text = greetingMessage
        weatherIcon.setImageResource(iconResId)

    }

    private fun getGreetingAndIcon(): Pair<String, Int> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 0..11 -> "Good Morning!" to R.drawable.baseline_wb_sunny_24
            in 12..17 -> "Good Afternoon!" to R.drawable.baseline_cloud_24
            else -> "Good Evening!" to R.drawable.baseline_mode_night_24
        }
    }
}