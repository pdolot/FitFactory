package com.example.fitfactory.presentation.navigationDrawer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.activities.LoginActivity
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.item_navigation.view.*
import javax.inject.Inject

class NavigationRecyclerViewAdapter(
    private var itemList: List<NavigationItem>?,
    private val drawerLayout: CustomDrawerLayout?,
    private val navController: NavController
) :
    RecyclerView.Adapter<NavigationRecyclerViewAdapter.NavigationViewHolder>() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var activity: AppCompatActivity

    @Inject
    lateinit var user: User

    @Inject
    lateinit var googleClient: GoogleSignInClient

    init {
        Injector.component.inject(this)
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


        if (position == itemCount - 1) viewHolder.itemView.navigationItem_separator.visibility =
            View.INVISIBLE

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

        item?.destinationId?.let { destination ->
            viewHolder.itemView.setOnClickListener {
                drawerLayout?.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                    override fun onDrawerClosed(drawerView: View) {
                        drawerLayout.removeDrawerListener(this)
                        if (navController.currentDestination?.id != destination) {
                            navController.navigate(destination)
                        }
                        super.onDrawerClosed(drawerView)
                    }
                })
                drawerLayout?.closeDrawer(GravityCompat.START, true)
            }
        }

        if (item?.destinationId == null) {
            viewHolder.itemView.setOnClickListener {
                LoginManager.getInstance().logOut()
                googleClient.signOut()
                moveToSignIn()
            }
        }

    }

    private fun moveToSignIn() {
        val loginActivity = Intent(activity, LoginActivity::class.java)
        loginActivity.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        loginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        loginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        loginActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(loginActivity)
        activity.finish()
    }

    inner class NavigationViewHolder internal constructor(view: View) :
        RecyclerView.ViewHolder(view)
}