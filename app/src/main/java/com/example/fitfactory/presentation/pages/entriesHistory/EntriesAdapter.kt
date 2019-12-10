package com.example.fitfactory.presentation.pages.entriesHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.Entry
import kotlinx.android.synthetic.main.item_entry.view.*

class EntriesAdapter : RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {

    private var items: List<Entry>? = null

    fun setData(items: List<Entry>?) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_entry, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position)
        holder.itemView.apply {
            fitnessClub_name.text = item?.name
            entryDate.text = item?.date
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}