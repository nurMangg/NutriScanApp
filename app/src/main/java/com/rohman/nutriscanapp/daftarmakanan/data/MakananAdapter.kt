package com.rohman.nutriscanapp.daftarmakanan.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.databinding.ItemListMakananBinding

class MakananAdapter(private val makananList: List<MakananResponseItem>) : RecyclerView.Adapter<MakananAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        // Tambahkan view lainnya yang Anda butuhkan
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_makanan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val makanan = makananList[position]
        holder.nameTextView.text = makanan.name
        holder.descriptionTextView.text = makanan.description
        // Set data lainnya ke view
    }

    override fun getItemCount() = makananList.size
}


