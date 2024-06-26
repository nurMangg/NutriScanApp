package com.rohman.nutriscanapp


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rohman.nutriscanapp.daftarmakanan.DaftarMakananActivity
import com.rohman.nutriscanapp.databinding.ActivityDashboardBinding
import com.rohman.nutriscanapp.pengaturan.PengaturanActivity
import com.rohman.nutriscanapp.riwayat.RiwayatActivity
import com.rohman.nutriscanapp.scan.CameraActivity
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

        playAnimation()
        setViewContent()
    }


    private fun getGreetingAndIcon(): Pair<String, Int> {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 3..11 -> "Good Morning!" to R.drawable.baseline_wb_sunny_24
            in 12..17 -> "Good Afternoon!" to R.drawable.baseline_cloud_24
            else -> "Good Evening!" to R.drawable.baseline_mode_night_24
        }
    }

    private fun setViewContent() {
        binding.btnScan.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
        binding.btnRiwayat.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
        binding.btnDaftarmakanan.setOnClickListener {
            startActivity(Intent(this, DaftarMakananActivity::class.java))
        }
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(this, PengaturanActivity::class.java))
        }

        binding.iconView.setOnClickListener { dialogPopUp() }
    }


    private fun playAnimation() {
        val foodDashboard =
            ObjectAnimator.ofFloat(binding.foodDashboard, View.ALPHA, 0f, 1f).setDuration(100)
        val info = ObjectAnimator.ofFloat(binding.iconView, View.ALPHA, 0f, 1f).setDuration(300)
        val weatherIcon =
            ObjectAnimator.ofFloat(binding.weatherIcon, View.ALPHA, 0f, 1f).setDuration(200)
        val greeting =
            ObjectAnimator.ofFloat(binding.greetingTextView, View.ALPHA, 0f, 1f).setDuration(200)
        val titleHead =
            ObjectAnimator.ofFloat(binding.titleHead, View.ALPHA, 0f, 1f).setDuration(200)
        val gridLayout =
            ObjectAnimator.ofFloat(binding.gridLayout, View.ALPHA, 0f, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                foodDashboard,
                titleHead,
                gridLayout,
                weatherIcon,
                greeting,
                info
            )
            startDelay = 100
        }.start()
    }


    private fun dialogPopUp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_info)

        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog.window?.setLayout(width, height)

        val closeButton: Button = dialog.findViewById(R.id.closeButton)
        val textView: TextView = dialog.findViewById(R.id.popupText)
        textView.text = getString(R.string.info_nutriscan)

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}