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
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.customViews.CustomDrawerLayout
import com.example.fitfactory.presentation.customViews.TopBar
import com.example.fitfactory.presentation.customViews.flexibleLayout.FlexibleView
import com.example.fitfactory.presentation.navigationDrawer.NavigationDrawer
import com.example.fitfactory.presentation.navigationDrawer.NavigationItem
import com.example.fitfactory.presentation.navigationDrawer.NavigationRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainInterface {

    @Inject
    lateinit var localStorage: LocalStorage

    private val adapter by lazy { NavigationRecyclerViewAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).setCurrentActivity(this)
        Injector.component.inject(this)
        setListeners()
        setTopBarProfileImage()
        setNavigationDrawer()
        localStorage.isLoggedLive().observe(this, Observer {
            mainFragment_navigationDrawer.setHeader(it)
            adapter.setData(getMenuList(it))
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
        localStorage.getUser()?.profileImage?.let {
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
            it?.let {
                if (it == R.id.returnToSource) localStorage.setToken(null)
                closeDrawer(it)
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

        mainFragment_topBar.topBarTitleChangeListener = {
            adapter.setActiveItem(it)
        }
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

    override fun getNavigationDrawer(): NavigationDrawer {
        return mainFragment_navigationDrawer
    }

    override fun onBackPressed() {
        val host = supportFragmentManager.findFragmentById(R.id.main_host_fragment)
        if (host?.childFragmentManager?.backStackEntryCount == 0) {
            this.moveTaskToBack(true)
        } else {
            super.onBackPressed()
        }
    }

    private fun getMenuList(isLogged: Boolean): List<NavigationItem> {
        val map = NavigationItem(
            getString(R.string.map),
            R.drawable.ic_map,
            R.id.mapFragment,
            getString(R.string.app_name)
        )
        val passes = NavigationItem(
            getString(R.string.yoursActivity),
            R.drawable.ic_pass,
            R.id.yourActivity,
            getString(R.string.yoursActivity)
        )
        val buyPass = NavigationItem(
            getString(R.string.buy_pass),
            R.drawable.ic_shop,
            R.id.buyPassFragment,
            getString(R.string.buy_pass)
        )
        val exercises = NavigationItem(
            getString(R.string.exercises),
            R.drawable.ic_fit_exercises,
            R.id.exercisesInfo,
            getString(R.string.exercises)
        )
        val fitnessLessons =
            NavigationItem(
                getString(R.string.fitness_lessons),
                R.drawable.ic_fitness_lesson,
                R.id.fitnessLesson,
                getString(R.string.fitness_lessons)
            )
        val history = NavigationItem(
            getString(R.string.history),
            R.drawable.ic_history,
            R.id.entriesHistory,
            getString(R.string.history)
        )
        val settings = NavigationItem(
            getString(R.string.settings),
            R.drawable.ic_settings,
            R.id.editProfile,
            getString(R.string.editProfile)
        )
        val signOut = NavigationItem(
            getString(R.string.sign_out),
            R.drawable.ic_arrow_left,
            R.id.returnToSource
        )

        return if (isLogged) listOf(
            map,
            passes,
            buyPass,
            exercises,
            fitnessLessons,
            history,
            settings,
            signOut
        ) else listOf(map, buyPass, exercises, fitnessLessons)
    }

}
