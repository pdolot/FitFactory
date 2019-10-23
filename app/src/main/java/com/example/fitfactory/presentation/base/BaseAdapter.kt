package com.example.fitfactory.presentation.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    abstract fun getTitle(position: Int): String?

    abstract fun setCurrentItem(position: Int)
}