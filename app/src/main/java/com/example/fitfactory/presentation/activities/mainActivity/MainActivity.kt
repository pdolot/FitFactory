package com.example.fitfactory.presentation.activities.mainActivity

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.example.fitfactory.R
import com.example.fitfactory.app.App
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.components.TopBar
import com.example.fitfactory.presentation.fragments.navigationFragment.NavigationFragment
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
        setNavigationView()
    }

    private fun setNavigationView() {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.navigation_root, NavigationFragment.newInstance())
        ft.commit()
    }

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

    override fun onBackPressed() {
        val host = supportFragmentManager.findFragmentById(R.id.main_host_fragment)
        if (host?.childFragmentManager?.backStackEntryCount == 0) {
            this.moveTaskToBack(true)
        }else{
            super.onBackPressed()
        }
    }
}
