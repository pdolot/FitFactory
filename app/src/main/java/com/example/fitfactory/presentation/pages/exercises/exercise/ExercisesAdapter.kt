package com.example.fitfactory.presentation.pages.exercises.exercise

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.Exercise
import com.example.fitfactory.presentation.base.BaseAdapter
import kotlinx.android.synthetic.main.item_exercise.view.*

class ExercisesAdapter : BaseAdapter<ExercisesAdapter.ViewHolder>() {

    override fun getTitle(position: Int): String? {
        return (items?.get(position) as Exercise).name ?: "Cwiczenie"
    }

    override fun setCurrentItem(position: Int) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items?.get(position) as Exercise
        val adapter by lazy { ImageAdapter() }
        holder.itemView.apply {
            movement.text = item.movement.joinToString(separator = "\n")
            tips.text = item.tips.joinToString(separator = "\n")
            startPosition.text = item.startPosition.joinToString(separator = "\n")
            imagesRv.adapter = adapter
            imagesRv.layoutManager = LinearLayoutManager(context)
            item.images.let { adapter.setData(it) }
        }
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
}