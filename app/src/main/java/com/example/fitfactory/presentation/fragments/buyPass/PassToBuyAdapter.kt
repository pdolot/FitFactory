package com.example.fitfactory.presentation.fragments.buyPass

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfactory.R
import com.example.fitfactory.data.models.PassType
import com.example.fitfactory.di.Injector
import kotlinx.android.synthetic.main.item_pass_to_buy.view.*
import javax.inject.Inject
import kotlin.math.roundToInt

class PassToBuyAdapter(private val passes: List<PassType>) : RecyclerView.Adapter<PassToBuyAdapter.ViewHolder>() {

    @Inject
    lateinit var activity: AppCompatActivity

    private var currentItem: Int = 0

    init {
        Injector.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pass_to_buy, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = passes.size

    fun getTitle(position: Int): String? = passes[position].passName

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =  passes[position]
        holder.itemView.apply {
            shadow.visibility = if (position == currentItem) View.INVISIBLE else View.VISIBLE
            Glide.with(context)
                .load("https://img3.goodfon.com/wallpaper/big/b/b2/ivan-kochetkov-kulturizm.jpg")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(pass_image)
            passDuration.text = resources.getQuantityString(R.plurals.months_plural,(item.durationInDays / 31.0).roundToInt(),(item.durationInDays / 31.0).roundToInt())
            passFullPrice.text = context.getString(R.string.price, item.price)
            passDescription.text = item.description
            passBenefits.text = item.benefits.joinToString(separator = "\n- ", prefix = "- ")
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

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
}