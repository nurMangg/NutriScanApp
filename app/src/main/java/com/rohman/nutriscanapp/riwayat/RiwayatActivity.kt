package com.rohman.nutriscanapp.riwayat

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.data.database.ResultDeteksi
import com.rohman.nutriscanapp.data.database.ResultDeteksiRoomDatabase
import com.rohman.nutriscanapp.data.repository.ResultDeteksiRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RiwayatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextDate: EditText
    private lateinit var riwayatAdapter: RiwayatAdapter

    private lateinit var totalKarbo: TextView
    private lateinit var totalProtein: TextView
    private lateinit var totalKalori: TextView
    private lateinit var totalLemak: TextView
    private lateinit var emptyDataTextView: TextView

    private val riwayatViewModel: RiwayatViewModel by viewModels {
        RiwayatViewModelFactory(ResultDeteksiRepository(ResultDeteksiRoomDatabase.getDatabase(application).resultDatabaseDao()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        editTextDate = findViewById(R.id.editTextDate)
        recyclerView = findViewById(R.id.rv_riwayat)
        totalKarbo = findViewById(R.id.total_karbo)
        totalProtein = findViewById(R.id.total_protein)
        totalKalori = findViewById(R.id.nilai_kalori)
        totalLemak = findViewById(R.id.total_lemak)
        emptyDataTextView = findViewById(R.id.emptyDataTextView)

        riwayatAdapter = RiwayatAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = riwayatAdapter

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

        riwayatViewModel.allData.observe(this, Observer { resultDeteksiList ->
            resultDeteksiList?.let { riwayatAdapter.submitList(it)
            updateNutrientTotals(it)}
        })


    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.time)
            editTextDate.setText(selectedDate)

            riwayatViewModel.getDataByDate(selectedDate).observe(this, Observer { resultDeteksiList ->
                resultDeteksiList?.let {
                    if (it.isEmpty()) {
                        emptyDataTextView.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        updateNutrientTotals(it)
                    } else {
                        emptyDataTextView.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        riwayatAdapter.submitList(it)
                        updateNutrientTotals(it)
                    }
                }
            })
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun updateNutrientTotals(resultDeteksiList: List<ResultDeteksi>) {
        var totalKarbohidrat = 0.0
        var totalProtein = 0.0
        var totalKalori = 0.0
        var totalLemak = 0.0

        if (resultDeteksiList.isNotEmpty()) {
            for (resultDeteksi in resultDeteksiList) {
                totalKarbohidrat += resultDeteksi.carbo?.toDouble() ?: 0.0
                totalProtein += resultDeteksi.protein?.toDouble() ?: 0.0
                totalKalori += resultDeteksi.calory?.toDouble() ?: 0.0
                totalLemak += resultDeteksi.lemak?.toDouble() ?: 0.0
            }
        } else {
            totalKarbohidrat = 0.0
            totalProtein = 0.0
            totalKalori = 0.0
            totalLemak = 0.0
        }

        this.totalKarbo.setText(String.format(Locale.US, "%.2f g", totalKarbohidrat))
        this.totalProtein.setText(String.format(Locale.US, "%.2f g", totalProtein))
        this.totalKalori.text = String.format(Locale.US, "%.2f kkal", totalKalori)
        this.totalLemak.text = String.format(Locale.US, "%.2f g", totalLemak)
    }

}