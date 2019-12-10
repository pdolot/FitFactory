package com.example.fitfactory.presentation.activities.mainActivity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainInterface {

    private val viewModel by lazy { MainViewModel() }
    private val adapter by lazy { NavigationRecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).setCurrentActivity(this)
        Injector.component.inject(this)
        setListeners()
        setTopBarProfileImage()
        setNavigationDrawer()
        viewModel.localStorage.isLoggedLive().observe(this, Observer {
            mainFragment_navigationDrawer.setHeader(it)
            adapter.setData(viewModel.getMenuList(it))
            if (it){
                mainFragment_navigationDrawer.setProfileView()
                setTopBarProfileImage()
            }else{
                mainFragment_topBar.setProfileImage(null)
            }
        })
    }


    private fun setNavigationDrawer() {
        mainFragment_navigationDrawer.setAdapter(adapter)
    }

    private fun setTopBarProfileImage() {
        viewModel.localStorage.getUser()?.profileImage?.let {
            mainFragment_topBar.setProfileImage(it)
        }

    }

    private fun setListeners() {
        mainFragment_topBar.setTopBarListeners(object : TopBar.TopBarListener {
            override fun onOptionsClick(isBackEnabled: Boolean) {
                if (isBackEnabled) {
                    findNavController(R.id.main_host_fragment).popBackStack()

                } else {
                    mainFragment_drawerLayout.openDrawer(GravityCompat.START)
                }
            }
        })

        adapter.onItemClick = {
            if (it != null) closeDrawer(it) else {
                closeDrawer(R.id.mapFragment)
                viewModel.localStorage.setToken(null)
            }
        }
        mainFragment_navigationDrawer.onSignInClick = { closeDrawer(R.id.signInFragment) }
        mainFragment_navigationDrawer.onSignUpClick = { closeDrawer(R.id.signUpFragment) }

        mainFragment_drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                mainFragment_navigationDrawer.resetScroll()
            }
        })
    }

    private fun closeDrawer(destinationId: Int) {
        mainFragment_drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerClosed(drawerView: View) {
                mainFragment_drawerLayout.removeDrawerListener(this)
                if (findNavController(R.id.main_host_fragment).currentDestination?.id != destinationId) {
                    findNavController(R.id.main_host_fragment).navigate(destinationId)
                }
                super.onDrawerClosed(drawerView)
            }
        })
        mainFragment_drawerLayout.closeDrawer(GravityCompat.START, true)
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

    override fun getFlexibleLayout(): FlexibleView {
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
