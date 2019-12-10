package com.example.fitfactory.presentation.pages.exercises.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseAdapter
import kotlinx.android.synthetic.main.item_exercise_image.view.*


class ImageAdapter : BaseAdapter<ImageAdapter.ViewHolder>() {

    override fun getTitle(position: Int): String? = null

    override fun setCurrentItem(position: Int){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position) as String
        holder.itemView.apply {
            Glide.with(context)
                .load(item)
                .centerCrop()
                .into(image)
        }
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
}