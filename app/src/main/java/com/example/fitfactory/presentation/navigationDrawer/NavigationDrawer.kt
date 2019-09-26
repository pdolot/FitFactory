package com.example.fitfactory.presentation.navigationDrawer

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfactory.R
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import kotlinx.android.synthetic.main.navigation_layout.view.*
import javax.inject.Inject

class NavigationDrawer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    @Inject
    lateinit var user: User

    init {
        View.inflate(context, R.layout.navigation_layout, this)
        Injector.component.inject(this)
    }

    fun setProfileView() {
        navigation_profileView.setProfileImage(Uri.parse(user.picture))
        navigation_profileView.setLevel(324)
        navigation_userName.text = "${user.firstName} ${user.lastName}"
    }

    fun setAdapter(navigationAdapter: NavigationRecyclerViewAdapter) {
        navigation_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = navigationAdapter
        }
    }
}