package com.rohman.nutriscanapp


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rohman.nutriscanapp.daftarmakanan.DaftarMakananActivity
import com.rohman.nutriscanapp.databinding.ActivityDashboardBinding
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

        setViewContent()
        main()
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

    private fun setViewContent(){
        binding.btnScan.setOnClickListener{
            startActivity(Intent(this, CameraActivity::class.java))
        }
        binding.btnRiwayat.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
        binding.btnDaftarmakanan.setOnClickListener {
            startActivity(Intent(this, DaftarMakananActivity::class.java))
        }

        binding.iconView.setOnClickListener{ dialogPopUp() }
    }

    private fun dialogPopUp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.popup_info)

        val width = resources.getDimensionPixelSize(R.dimen.popup_width)
        val height = resources.getDimensionPixelSize(R.dimen.popup_height)
        dialog.window?.setLayout(width, height)

        val closeButton: Button = dialog.findViewById(R.id.closeButton)
        val textView: TextView = dialog.findViewById(R.id.popupText)
        textView.text = "Some text here"

        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun main() {
        println("No Idea, I Need Healing")
    }


}