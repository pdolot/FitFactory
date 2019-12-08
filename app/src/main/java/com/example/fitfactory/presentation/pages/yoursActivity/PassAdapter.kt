package com.example.fitfactory.presentation.pages.yoursActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.Pass
import com.example.fitfactory.di.Injector
import kotlinx.android.synthetic.main.item_pass.view.*
import javax.inject.Inject

class PassAdapter : RecyclerView.Adapter<PassAdapter.ViewHolder>() {

    @Inject
    lateinit var activity: AppCompatActivity
    private var passes: List<Pass>? = null
    private var filteredPasses: List<Pass>? = null

    init {
        Injector.component.inject(this)
        filterPasses(true)
    }

    fun setData(passes: List<Pass>){
        this.passes = passes
        filterPasses(true)
    }

    fun filterPasses(isActive: Boolean){
        filteredPasses = passes?.filter { pass -> pass.isActive == isActive }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pass, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filteredPasses?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredPasses?.get(position)
        holder.itemView.apply {
            item?.isActive?.let {
                contract_termination.visibility = if (it) View.VISIBLE else View.GONE
            }
            Glide.with(context)
                .load(item?.qrCode)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(qrCode)
            passName.text = item?.passType?.shortName
            passDuration.text = context.getString(R.string.duration, item?.startDate, item?.endDate)
            passPrice.text = context.getString(R.string.pricePerMonth, item?.passType?.periodPrice)

            contract_termination.setOnClickListener {
                MaterialDialog(context).show {
                    title(text = "Czy na pewno chcesz zerwać umowę?")
                    message(text = "Za zerwanie umowy zostanie naliczona kara zgodnie z przelicznikiem. Po naciśnięciu opcji ZERWIJ zostaniesz przeniesiony do ekranu zapłaty")
                    positiveButton(text = "ZERWIJ"){
                        //TODO
                    }
                    negativeButton(text = "ANULUJ"){
                        dismiss()
                    }
                }
            }

            reBuyPass.setOnClickListener {
                findNavController().navigate(R.id.paymentFragment)
            }
        }
    }

    inner class ViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view)
}