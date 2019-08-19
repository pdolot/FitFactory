package com.example.fitfactory.presentation.fragments.navigationFragment

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfactory.R
import com.example.fitfactory.data.models.User
import com.example.fitfactory.di.Injector
import kotlinx.android.synthetic.main.navigation_layout.*
import javax.inject.Inject

class NavigationFragment : Fragment() {
    @Inject
    lateinit var user: User

    private lateinit var navigationAdapter: NavigationRecyclerViewAdapter
    private var menuItemList: List<NavigationItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenuList()
        setAdapter()
        setProfileView()
    }

    private fun setProfileView() {
        navigation_profileView.setProfileImage(Uri.parse(user.picture))
        navigation_profileView.setLevel(324)
        navigation_userName.text = "${user.firstName} ${user.lastName}"
    }

    private fun initMenuList() {
        val map = NavigationItem(getString(R.string.map), R.drawable.ic_map, R.id.mapFragment)
        val passes = NavigationItem(getString(R.string.passes), R.drawable.ic_pass, R.id.mapFragment)
        val buyPass = NavigationItem(getString(R.string.buy_pass), R.drawable.ic_shop, R.id.mapFragment)
        val exercises = NavigationItem(getString(R.string.exercises), R.drawable.ic_fit_exercises, R.id.mapFragment)
        val fitnessLessons =
            NavigationItem(getString(R.string.fitness_lessons), R.drawable.ic_fitness_lesson, R.id.mapFragment)
        val history = NavigationItem(getString(R.string.history), R.drawable.ic_history, R.id.mapFragment)
        val settings = NavigationItem(getString(R.string.settings), R.drawable.ic_settings, R.id.mapFragment)
        val signOut = NavigationItem(getString(R.string.sign_out), R.drawable.ic_arrow_left, null)

        menuItemList = listOf(map, passes, buyPass, exercises, fitnessLessons, history, settings, signOut)

    }

    private fun setAdapter() {
        navigationAdapter = NavigationRecyclerViewAdapter(menuItemList)
        navigation_recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = navigationAdapter
        }
    }

    companion object {
        fun newInstance() = NavigationFragment()
    }
}