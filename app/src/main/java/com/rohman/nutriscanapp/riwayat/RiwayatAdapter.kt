package com.rohman.nutriscanapp.riwayat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rohman.nutriscanapp.R
import com.rohman.nutriscanapp.data.database.ResultDeteksi

class RiwayatAdapter : ListAdapter<ResultDeteksi, RiwayatAdapter.RiwayatViewHolder>(RiwayatDiffCallback()) {

    private var nutrientData: List<ResultDeteksi> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiwayatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_riwayat, parent, false)
        return RiwayatViewHolder(view)
    }

    override fun onBindViewHolder(holder: RiwayatViewHolder, position: Int) {
        val resultDeteksi = getItem(position)
        holder.bind(resultDeteksi)
    }

    inner class RiwayatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNama: TextView = itemView.findViewById(R.id.nameTextView)
        private val tvDesc: TextView = itemView.findViewById(R.id.descTextView)
        private val tvDate: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(resultDeteksi: ResultDeteksi) {
            tvNama.text = resultDeteksi.name
            tvDesc.text = resultDeteksi.desc
            tvDate.text = resultDeteksi.tanggal
        }
    }

}

class RiwayatDiffCallback : DiffUtil.ItemCallback<ResultDeteksi>() {
    override fun areItemsTheSame(oldItem: ResultDeteksi, newItem: ResultDeteksi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ResultDeteksi, newItem: ResultDeteksi): Boolean {
        return oldItem == newItem
    }
}