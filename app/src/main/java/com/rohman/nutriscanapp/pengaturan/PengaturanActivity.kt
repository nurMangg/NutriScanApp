package com.rohman.nutriscanapp.pengaturan

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rohman.nutriscanapp.R


class PengaturanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengaturan)

        val feedbackButton = findViewById<RelativeLayout>(R.id.feedback)
        val about = findViewById<RelativeLayout>(R.id.about)

        feedbackButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("withmangg@email.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            }

            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(emailIntent, "Kirim Email Menggunakan:"))
            } else {
                Toast.makeText(this, "Tidak ada aplikasi email yang terinstal.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        about.setOnClickListener {
            startActivity(Intent(this, TentangNutriscanActivity::class.java))
        }
    }
}
