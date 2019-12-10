package com.example.fitfactory.presentation.pages.fitnessLesson

import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.FitnessLesson
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseAdapter
import kotlinx.android.synthetic.main.item_fitness_lesson.view.*
import javax.inject.Inject

class FitnessLessonAdapter : BaseAdapter<FitnessLessonAdapter.ViewHolder>() {

    @Inject
    lateinit var activity: AppCompatActivity

    private var currentItem: Int = 0

    private var filteredData: List<FitnessLesson>? = null

    init {
        Injector.component.inject(this)
    }

    fun filterData(fitnessClubId: Long?){
        filteredData = if (fitnessClubId == null) {
            items?.map { it as FitnessLesson }
        }else{
            items?.map { it as FitnessLesson }?.filter { it.fitnessClub?.id == fitnessClubId}
        }
        notifyDataSetChanged()
        onDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_fitness_lesson, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredData?.size ?: 0

    override fun getTitle(position: Int): String? {
        return filteredData?.get(position)?.name ?: ""
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item =  filteredData?.get(position)
        item?.let {
            holder.itemView.apply {
                val dataNotGive = context.getString(R.string.data_not_given).toLowerCase()

                shadow.visibility = if (position == currentItem) View.INVISIBLE else View.VISIBLE
                Glide.with(context)
                    .load(it.coach?.profileImage)
                    .placeholder(R.drawable.coach_placeholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(coach_profileImage)

                Glide.with(context)
                    .load(it.promoImage)
                    .placeholder(R.drawable.fitnesslesson_placeholder)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(fitnessLesson_promoImage)

                fitnessClub_name.text = it.fitnessClub?.name
                coach_firstName_lastName.text = "${it.coach?.firstName} ${it.coach?.lastName}"
                coach_email.text = it.coach?.email ?: dataNotGive
                coach_phone.text = it.coach?.phoneNumber ?: dataNotGive
                date.text = it.date
                price.text = context.getString(R.string.price, item.price)
                peopleLimit.text = it.peopleLimit.toString()
                signedUpPeopleCount.text = it.signedUpPeopleCount.toString()
                signedUpPeopleCountProgressBar.progress = (it.signedUpPeopleCount?.toFloat() ?: 0.0f) / (it.peopleLimit?.toFloat() ?: 1.0f)
                description.text = it.description

            }
            setCellWidth(holder, position)
        }
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

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}