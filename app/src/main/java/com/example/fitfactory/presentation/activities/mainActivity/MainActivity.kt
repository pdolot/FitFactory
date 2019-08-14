package com.example.fitfactory.presentation.activities.mainActivity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.presentation.components.TopBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        (application as App).setCurrentActivity(this)
        setListeners()
        setTopBarProfileImage()
    }

    private fun setTopBarProfileImage() {
        mainFragment_topBar.setProfileImage(null)
    }

    private fun setListeners() {
        mainFragment_topBar.setTopBarListeners(object : TopBar.TopBarListener{
            override fun onOptionsClick() {
                mainFragment_drawerLayout.openDrawer(GravityCompat.START)
            }
        })
    }

    override var actions: MainFragmentInterface?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun getTopBar(): TopBar? {
        return mainFragment_topBar
    }
}
