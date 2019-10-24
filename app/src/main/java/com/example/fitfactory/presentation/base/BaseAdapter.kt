package com.example.fitfactory.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    var items: List<Any>? = null

    fun setData(data: List<Any>){
        items = data
        notifyDataSetChanged()
    }

    abstract fun getTitle(position: Int): String?

    abstract fun setCurrentItem(position: Int)
}