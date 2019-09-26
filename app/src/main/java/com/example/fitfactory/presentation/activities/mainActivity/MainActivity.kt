package com.example.fitfactory.presentation.activities.mainActivity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.FlexibleBottomView
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainInterface {


    @Inject
    lateinit var user: User

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        (application as App).setCurrentActivity(this)
        Injector.component.inject(this)
        setListeners()
        setTopBarProfileImage()
        setNavigationDrawer()
    }

    private fun setNavigationDrawer() {
        mainFragment_navigationDrawer.setProfileView()
        mainFragment_navigationDrawer.setAdapter(setAdapter())
    }

    private fun setAdapter(): NavigationRecyclerViewAdapter =
        NavigationRecyclerViewAdapter(
            viewModel.getMenuList(),
            mainFragment_drawerLayout,
            findNavController(R.id.main_host_fragment)
        )

    private fun setTopBarProfileImage() {
        mainFragment_topBar.setProfileImage(Uri.parse(user.picture))
    }

    private fun setListeners() {
        mainFragment_topBar.setTopBarListeners(object : TopBar.TopBarListener {
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

    override fun getDrawer(): CustomDrawerLayout? {
        return mainFragment_drawerLayout
    }

    override fun getFlexibleLayout(): FlexibleBottomView {
        return mainFragment_flexibleLayout
    }

    override fun onBackPressed() {
        val host = supportFragmentManager.findFragmentById(R.id.main_host_fragment)
        if (host?.childFragmentManager?.backStackEntryCount == 0) {
            this.moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }
}
