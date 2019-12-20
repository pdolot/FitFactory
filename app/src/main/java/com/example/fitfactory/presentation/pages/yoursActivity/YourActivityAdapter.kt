package com.example.fitfactory.presentation.pages.yoursActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseAdapter
import com.example.fitfactory.presentation.pages.yoursActivity.fitnessLesson.FitnessLessonSimpleAdapter
import com.example.fitfactory.presentation.pages.yoursActivity.passes.PassAdapter
import kotlinx.android.synthetic.main.item_fitness_lessons.view.*
import kotlinx.android.synthetic.main.item_passes.view.*

class YourActivityAdapter : BaseAdapter<YourActivityAdapter.ViewHolder>() {

    var currentPosition = 0
    val passesAdapter by lazy { PassAdapter() }
    val fitnessLessonAdapter by lazy { FitnessLessonSimpleAdapter() }

    override fun getTitle(position: Int): String? {
        return when(position){
            0 -> "Karnety"
            1 -> "Zajęcia fitness"
            else -> null
        }
    }

    override fun setCurrentItem(position: Int) {
        this.currentPosition = position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate( when(viewType){
            ViewType.PASSES.ordinal -> R.layout.item_passes
            ViewType.LESSONS.ordinal -> R.layout.item_fitness_lessons
            else -> R.layout.item_empty_data
        }, parent, false)
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> ViewType.PASSES.ordinal
            1 -> ViewType.LESSONS.ordinal
            else -> ViewType.EMPTY_DATA.ordinal
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType){
            ViewType.PASSES.ordinal -> {
                holder.itemView.apply {
                    recyclerView_passes.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = this@YourActivityAdapter.passesAdapter
                    }
                }
            }
            ViewType.LESSONS.ordinal -> {
                holder.itemView.apply {
                    recyclerView_fitnessLessons.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = this@YourActivityAdapter.fitnessLessonAdapter
                    }
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    enum class ViewType{
        PASSES,
        LESSONS,
        EMPTY_DATA
    }
}