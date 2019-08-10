package com.example.fitfactory.presentation.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.fitfactory.R
import com.example.fitfactory.presentation.components.TopBar
import com.example.fitfactory.presentation.fragments.mainFragment.MainFragmentInterface
import com.example.fitfactory.presentation.fragments.mainFragment.MainInterface
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity(), MainInterface {
    override var actions: MainFragmentInterface?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun getTopBar(): TopBar? = mainFragment_topBar

    override fun getNavController(): NavController? = findNavController(R.id.drawer_host_fragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        findNavController(R.id.nav_host_fragment).navigate(R.id.mapFragment)
    }

}
