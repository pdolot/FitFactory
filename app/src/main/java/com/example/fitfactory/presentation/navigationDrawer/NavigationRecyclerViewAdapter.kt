package com.example.fitfactory.presentation.navigationDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.item_navigation.view.*
import javax.inject.Inject

class NavigationRecyclerViewAdapter :
    RecyclerView.Adapter<NavigationRecyclerViewAdapter.NavigationViewHolder>() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var activity: AppCompatActivity

    @Inject
    lateinit var googleClient: GoogleSignInClient

    private var itemList: List<NavigationItem>? = null

    var onItemClick: (destinationId: Int?) -> Unit = {}

    private var activeItem: Int = 0

    init {
        Injector.component.inject(this)
    }

    fun setData(itemList: List<NavigationItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun setActiveItem(title: String){
        activeItem = this.itemList?.indexOfFirst { it.topBarTitle == title } ?: 0
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NavigationViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(if (viewType == 0) R.layout.item_navigation else R.layout.item_navigation_active, viewGroup, false)
        return NavigationViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == activeItem) 1 else 0
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: NavigationViewHolder, position: Int) {

        val item = itemList?.get(position)

        item?.let {
            viewHolder.itemView.navigationItem_name.text = it.name
            it.iconId?.let { iconId ->
                viewHolder.itemView.navigationItem_icon.setImageDrawable(
                    context.getDrawable(
                        iconId
                    )
                )
            }
        }


        viewHolder.itemView.setOnClickListener {
            onItemClick(item?.destinationId)
        }
    }

    inner class NavigationViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view)

}