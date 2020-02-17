package com.example.fitfactory.presentation.pages.yoursActivity.fitnessLesson

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.LessonEntry
import com.example.fitfactory.data.rest.RetrofitRepository
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.pages.payment.fitnessLessonPayment.FitnessLessonPaymentDirections
import com.example.fitfactory.utils.TimeUtil
import kotlinx.android.synthetic.main.item_simply_fitness_lesson.view.*
import javax.inject.Inject

class FitnessLessonSimpleAdapter : RecyclerView.Adapter<FitnessLessonSimpleAdapter.ViewHolder>() {

    @Inject
    lateinit var activity: AppCompatActivity

    var onSignOutListener: (entryId: Long) -> Unit = {}

    private var items: List<Any>? = null

    init {
        Injector.component.inject(this)

    }

    fun setData(items: List<Any>?) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                when (viewType) {
                    1 -> R.layout.item_simply_fitness_lesson
                    else -> R.layout.item_empty_data
                }, parent, false
            )
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items?.get(position)) {
            is LessonEntry -> 1
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (holder.itemViewType) {
            1 -> {
                val item = items?.get(position) as LessonEntry
                holder.itemView.apply {
                    Glide.with(context)
                        .load(item.fitnessLesson?.promoImage)
                        .placeholder(R.drawable.fitnesslesson_placeholder)
                        .centerCrop()
                        .into(fitnessLesson_promoImage)

                    fitnessLesson_name.text = item.fitnessLesson?.name
                    fitnessLesson_date.text = item.fitnessLesson?.date
                    wasPresent.drawable.setTint(
                        if (item.wasPresent) ContextCompat.getColor(
                            context,
                            R.color.positiveLight
                        ) else ContextCompat.getColor(context, R.color.primaryBgColor5)
                    )
                    isPaid.drawable.setTint(
                        if (item.isPaid) ContextCompat.getColor(
                            context,
                            R.color.positiveLight
                        ) else ContextCompat.getColor(context, R.color.primaryBgColor5)
                    )

                    signOutFromLesson.visibility =
                        if (TimeUtil.isBeforeNow(
                                item.fitnessLesson?.date,
                                "dd/MM/yyyy HH:mm"
                            )
                        ) View.VISIBLE else View.GONE

                    isPaid.setOnClickListener {
                        if (!item.isPaid) {
                            MaterialDialog(activity).show {
                                title(text = "Zajęcia fitness")
                                message(text = "Czy chcesz zapłacić za zajęcia?")
                                positiveButton(text = "Tak") {
                                    findNavController().navigate(
                                        FitnessLessonPaymentDirections.toFitnessLessonPayment(
                                            item.id,
                                            item.fitnessLesson?.name ?: return@positiveButton,
                                            item.fitnessLesson.date ?: return@positiveButton,
                                            item.fitnessLesson.price?.toFloat() ?: return@positiveButton
                                        )
                                    )
                                }
                                negativeButton(text = "Nie")
                            }
                        }
                    }
                    signOutFromLesson.setOnClickListener {
                        MaterialDialog(activity).show {
                            title(text = "Zajęcia fitness")
                            message(text = "Czy na pewno chcesz się wypisać z zajęć?")
                            positiveButton(text = "Tak") {
                                onSignOutListener(item.id)
                            }
                            negativeButton(text = "Nie")
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}