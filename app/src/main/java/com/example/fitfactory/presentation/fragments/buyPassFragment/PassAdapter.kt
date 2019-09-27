package com.example.fitfactory.presentation.fragments.buyPassFragment

import android.graphics.Point
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.utils.scaleValue
import kotlinx.android.synthetic.main.club_bar.view.*
import kotlinx.android.synthetic.main.fragment_pass.view.*
import javax.inject.Inject

class PassAdapter(private val passes: List<Pass>) : RecyclerView.Adapter<PassAdapter.ViewHolder>() {

    @Inject
    lateinit var activity: AppCompatActivity

    private var currentItem: Int = 0

    init {
        Injector.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_pass, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = passes.size

    fun getTitle(position: Int): String? = passes[position].name

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            shadow.visibility = if (position == currentItem) View.INVISIBLE else View.VISIBLE
            Glide.with(context)
                .load("https://img3.goodfon.com/wallpaper/big/b/b2/ivan-kochetkov-kulturizm.jpg")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(pass_image)
        }
        setCellWidth(holder, position)
    }

    fun setCurrentItem(position: Int) {
        this.currentItem = position
        notifyDataSetChanged()
    }

    private fun setCellWidth(vh: ViewHolder, position: Int) {
        vh.itemView.layoutParams =
            (vh.itemView.layoutParams as? ViewGroup.MarginLayoutParams)?.apply {
                val point = Point().apply { activity.windowManager.defaultDisplay.getSize(this) }
                val screenWidth = point.x
                width = (screenWidth * 0.82).toInt()

                marginStart =
                    if (position == 0) (screenWidth * 0.09).toInt() else (screenWidth * 0.02).toInt()
                marginEnd =
                    if (position == (itemCount - 1)) (screenWidth * 0.09).toInt() else (screenWidth * 0.02).toInt()
            }
    }

    inner class ViewHolder internal constructor(view: View, var guidelinePercent: Float = 0.6f) : RecyclerView.ViewHolder(view)
}