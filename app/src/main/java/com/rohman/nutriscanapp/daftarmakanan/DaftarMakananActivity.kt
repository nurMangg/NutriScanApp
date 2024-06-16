package com.rohman.nutriscanapp.daftarmakanan

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.daftarmakanan.data.MakananAdapter
import com.rohman.nutriscanapp.daftarmakanan.data.MakananResponse
import com.rohman.nutriscanapp.daftarmakanan.data.MakananResponseItem
import com.rohman.nutriscanapp.data.api.ApiConfig
import com.rohman.nutriscanapp.databinding.ActivityDaftarMakananBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarMakananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarMakananBinding

    companion object {
        private const val TAG = "DaftarMainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val layoutManager = LinearLayoutManager(this)
        binding.makanan.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.makanan.addItemDecoration(itemDecoration)
        getListMakanan()
    }

    private fun getListMakanan() {
        showLoad(true)
        val client = ApiConfig.getApiService().getMakanan()
        client.enqueue(object : Callback<MakananResponse> {
            override fun onResponse(
                call: Call<MakananResponse>,
                response: Response<MakananResponse>
            ) {
                showLoad(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setReviewData(responseBody.data)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MakananResponse>, t: Throwable) {
                showLoad(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setReviewData(makananList: List<MakananResponseItem>) {
        val recyclerView: RecyclerView = findViewById(R.id.makanan)
        val adapter = MakananAdapter(makananList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun showLoad(isLoad: Boolean) {
        binding.progressBar.visibility = if (isLoad) View.VISIBLE else View.GONE
    }

}