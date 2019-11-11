package com.example.fitfactory.presentation.navigationDrawer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.UserGetResource
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

    init {
        Injector.component.inject(this)
    }

    fun setData(itemList: List<NavigationItem>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NavigationViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_navigation, viewGroup, false)
        return NavigationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: NavigationViewHolder, position: Int) {


//        if (position == itemCount - 1) viewHolder.itemView.navigationItem_separator.visibility =
//            View.INVISIBLE

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