package com.rohman.nutriscanapp.riwayat

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RiwayatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextDate: EditText
//    private lateinit var riwayatAdapter: RiwayatAdapter // Assume you have a custom adapter for RecyclerView
//    private var riwayatList: List<RiwayatItem> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riwayat)

        editTextDate = findViewById(R.id.editTextDate)
//        recyclerView = findViewById(R.id.recyclerViewRiwayat)

//        recyclerView.layoutManager = LinearLayoutManager(this)
//        riwayatAdapter = RiwayatAdapter(riwayatList)
//        recyclerView.adapter = riwayatAdapter

        editTextDate.setOnClickListener {
            showDatePickerDialog()
        }
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
//            filterRiwayatByDate(selectedDate)
        }, year, month, day)

        datePickerDialog.show()
    }

//    private fun filterRiwayatByDate(date: String) {
//        val filteredList = riwayatList.filter { it.date == date }
//        riwayatAdapter.updateData(filteredList)
//    }
}