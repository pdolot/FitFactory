package com.example.fitfactory.presentation.pages.yoursActivity.passes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.EmptyData
import com.example.fitfactory.data.models.app.Pass
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.customViews.passPreviewDialog.PassPreview
import com.example.fitfactory.presentation.pages.payment.passPayment.PaymentFragmentDirections
import com.example.fitfactory.utils.generateQrCode
import kotlinx.android.synthetic.main.item_pass.view.*
import javax.inject.Inject

class PassAdapter : RecyclerView.Adapter<PassAdapter.ViewHolder>() {

    private var items: List<Any>? = null

    @Inject
    lateinit var activity: AppCompatActivity
    private var filteredPasses: List<Pass>? = null

    var onContractTerminationClick: (id: Long, name: String) -> Unit = {id, name -> {}}

    init {
        Injector.component.inject(this)
    }

    fun setData(items: List<Any>) {
        this.items = items
        if (items.first() is Pass) {
            filterPasses(true)
        }
        notifyDataSetChanged()
    }

    fun filterPasses(isActive: Boolean) {
        filteredPasses = items?.map { it as Pass }?.filter { pass -> pass.isActive == isActive }
        if (filteredPasses.isNullOrEmpty()) {
            items = listOf(EmptyData())
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(
                when (viewType) {
                    1 -> R.layout.item_pass
                    else -> R.layout.item_empty_data
                }, parent, false
            )
        return ViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items?.get(position)) {
            is Pass -> 1
            else -> 0
        }
    }

    override fun getItemCount(): Int {
        if (items?.first() is Pass) {
            return filteredPasses?.size ?: 0
        }
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder.itemViewType) {
            1 -> {
                val item = filteredPasses?.get(position)
                holder.itemView.apply {
                    item?.isActive?.let {
                        contract_termination.visibility = if (it) View.VISIBLE else View.GONE
                    }
                    qrCode.generateQrCode(item?.qrCode)
                    passName.text = item?.passType?.shortName
                    passDuration.text =
                        context.getString(R.string.duration, item?.startDate, item?.endDate)
                    passPrice.text =
                        context.getString(R.string.pricePerMonth, item?.passType?.periodPrice)
                    userName.text = item?.passUser?.firstName + " " + item?.passUser?.lastName

                    contract_termination.setOnClickListener {
                        MaterialDialog(context).show {
                            title(text = "Czy na pewno chcesz zerwać umowę?")
                            message(text = "Za zerwanie umowy zostanie naliczona kara zgodnie z przelicznikiem. Po naciśnięciu opcji ZERWIJ zostaniesz przeniesiony do ekranu zapłaty")
                            positiveButton(text = "ZERWIJ") {
                                item?.id?.let {
                                    onContractTerminationClick(it, item.passType?.shortName ?: "")
                                }

                            }
                            negativeButton(text = "ANULUJ")
                        }
                    }

                    reBuyPass.setOnClickListener {
                        findNavController().navigate(
                            PaymentFragmentDirections.toPaymentFragment(
                                item?.passType?.id ?: return@setOnClickListener
                            )
                        )
                    }

                    setOnClickListener {
                        MaterialDialog(activity).show {
                            customView(view = PassPreview(activity).apply {
                                item?.let { pass = it }
                            }, scrollable = true)
                            positiveButton(text = "Zamknij")
                        }
                    }
                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}