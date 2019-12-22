package com.example.fitfactory.presentation.pages.buyPass

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.PassType
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseAdapter
import kotlinx.android.synthetic.main.item_pass_to_buy.view.*
import javax.inject.Inject
import kotlin.math.roundToInt




class PassToBuyAdapter : BaseAdapter<PassToBuyAdapter.ViewHolder>() {

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

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getTitle(position: Int): String? {

        items?.get(position)?.let {
            return when(it){
                is PassType -> it.shortName
                else -> ""
            }
        }
        return ""
    }

    fun getPassTypeId(): Long?{
        return (items?.get(currentItem) as PassType).id
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =  items?.get(position) as PassType
        holder.itemView.apply {
            shadow.visibility = if (position == currentItem) View.INVISIBLE else View.VISIBLE
            Glide.with(context)
                .load(item.promoImage)
                .placeholder(R.drawable.fitness_pass_promo)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(pass_image)
            passDuration.text = item.durationInDays?.let {
                resources.getQuantityString(R.plurals.months_plural,(it / 31.0).roundToInt(),(it / 31.0).roundToInt())
            } ?: context.getString(R.string.data_not_given)
            passFullPrice.text = context.getString(R.string.pricePerMonth, item.periodPrice)
            passDescription.text = item.description
            passBenefits.text = item.benefits?.joinToString(separator = "\n- ", prefix = "- ")
        }
        setCellWidth(holder, position)
    }

    override fun setCurrentItem(position: Int) {
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