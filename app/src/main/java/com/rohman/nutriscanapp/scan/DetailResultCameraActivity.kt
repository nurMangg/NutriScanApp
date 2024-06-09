package com.rohman.nutriscanapp.scan

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.databinding.ActivityDetailResultCameraBinding



class DetailResultCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailResultCameraBinding
    private var initialHeight = 0
    private var isBeingDragged = false
    private var initialTouchY = 0f

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

//        binding.btnMap.setOnClickListener {
//            startActivity(Intent(this, MapsActivity::class.java))
//        }
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
    }
}
