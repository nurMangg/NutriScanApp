package com.rohman.nutriscanapp

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rohman.nutriscanapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.startButton.setOnClickListener {
            playButtonClickAnimation {
                startActivity(Intent(this, DashboardActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset button properties to default values
        binding.startButton.alpha = 1f
        binding.startButton.scaleX = 1f
        binding.startButton.scaleY = 1f
    }

    private fun playAnimation() {
        // Translation animation
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f, 1f)
        ObjectAnimator.ofPropertyValuesHolder(binding.imageView, scaleX, scaleY).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val appTitle = ObjectAnimator.ofFloat(binding.appTitle, View.ALPHA, 0f, 1f).setDuration(200)
        val appDesc = ObjectAnimator.ofFloat(binding.appDesc, View.ALPHA, 0f, 1f).setDuration(300)
        val startButton =
            ObjectAnimator.ofFloat(binding.startButton, View.ALPHA, 0f, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                appTitle,
                appDesc,
                startButton
            )
            startDelay = 100
        }.start()
    }

    private fun playButtonClickAnimation(onEnd: () -> Unit) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f, 1f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f, 1f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)

        val buttonAnimation =
            ObjectAnimator.ofPropertyValuesHolder(binding.startButton, scaleX, scaleY, alpha)
                .apply {
                    duration = 500
                }

        buttonAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
                onEnd()
            }
        })

        buttonAnimation.start()
    }
}
