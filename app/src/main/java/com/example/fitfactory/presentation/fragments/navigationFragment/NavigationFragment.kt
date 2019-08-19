package com.example.fitfactory.presentation.fragments.navigationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.navigation_layout.*

class NavigationFragment : Fragment() {
    var count = 0
    private lateinit var navigationAdapter: NavigationRecyclerViewAdapter
    private var menuItemList: List<NavigationItem>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMenuList()
        setAdapter()
        navigation_profileView.setLevel(0)
    }

    private fun initMenuList() {
        val map = NavigationItem(getString(R.string.map), R.drawable.ic_map, R.id.mapFragment)
        val passes = NavigationItem(getString(R.string.passes), R.drawable.ic_pass, null)
        val buyPass = NavigationItem(getString(R.string.buy_pass), R.drawable.ic_shop, null)
        val exercises = NavigationItem(getString(R.string.exercises), R.drawable.ic_fit_exercises, null)
        val fitnessLessons =
            NavigationItem(getString(R.string.fitness_lessons), R.drawable.ic_fitness_lesson, null)
        val history = NavigationItem(getString(R.string.history), R.drawable.ic_history, null)
        val settings = NavigationItem(getString(R.string.settings), R.drawable.ic_settings, null)
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