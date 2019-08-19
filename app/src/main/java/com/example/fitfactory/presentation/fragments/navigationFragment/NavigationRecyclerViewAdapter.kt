package com.example.fitfactory.presentation.fragments.navigationFragment

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.activities.LoginActivity
import com.example.fitfactory.presentation.activities.mainActivity.MainActivity
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.navigation_item.view.*
import javax.inject.Inject

class NavigationRecyclerViewAdapter(private var itemList: List<NavigationItem>?) :
    RecyclerView.Adapter<NavigationRecyclerViewAdapter.NavigationViewHolder>() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var activity: AppCompatActivity

    @Inject
    lateinit var user: User

    init {
        Injector.component.inject(this)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NavigationViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.navigation_item, viewGroup, false)
        return NavigationViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemList?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: NavigationViewHolder, position: Int) {

        if (position == itemCount - 1) viewHolder.itemView.navigationItem_separator.visibility = View.INVISIBLE

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

        viewHolder.itemView.setOnClickListener { _ ->
            item?.destinationId?.let {
                activity.findViewById<DrawerLayout>(R.id.mainFragment_drawerLayout).apply {
                    closeDrawer(GravityCompat.START, true)
                    addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                        override fun onDrawerClosed(drawerView: View) {
                            super.onDrawerClosed(drawerView)
                            removeDrawerListener(this)
                            val navController = activity.findNavController(R.id.main_host_fragment)
                            if (navController.currentDestination?.id != it){
                                activity.findNavController(R.id.main_host_fragment).navigate(it)
                            }
                        }
                    })
                }
            }

            if (item?.destinationId == null){
                LoginManager.getInstance().logOut()
                moveToSignIn()
            }
        }
    }

    private fun moveToSignIn() {
        val loginActivity = Intent(activity, LoginActivity::class.java)
        loginActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        activity.startActivity(loginActivity)
        activity.finish()
    }

    inner class NavigationViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view)
}