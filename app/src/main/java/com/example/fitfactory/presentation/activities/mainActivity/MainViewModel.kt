package com.example.fitfactory.presentation.activities.mainActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.fitfactory.R
import com.example.fitfactory.di.Injector
import com.example.fitfactory.functional.localStorage.LocalStorage
import com.example.fitfactory.presentation.navigationDrawer.NavigationItem
import javax.inject.Inject

class MainViewModel : ViewModel() {

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var localStorage: LocalStorage

    init {
        Injector.component.inject(this)
    }

    fun getMenuList(isLogged: Boolean): List<NavigationItem> {
        val map = NavigationItem(
            context.getString(R.string.map),
            R.drawable.ic_map,
            R.id.mapFragment
        )
        val passes = NavigationItem(
            context.getString(R.string.yoursActivity),
            R.drawable.ic_pass,
            R.id.passFragment
        )
        val buyPass = NavigationItem(
            context.getString(R.string.buy_pass),
            R.drawable.ic_shop,
            R.id.buyPassFragment
        )
        val exercises = NavigationItem(
            context.getString(R.string.exercises),
            R.drawable.ic_fit_exercises,
            R.id.exercisesInfo
        )
        val fitnessLessons =
            NavigationItem(
                context.getString(R.string.fitness_lessons),
                R.drawable.ic_fitness_lesson,
                R.id.mapFragment
            )
        val history = NavigationItem(
            context.getString(R.string.history),
            R.drawable.ic_history,
            R.id.mapFragment
        )
        val settings = NavigationItem(
            context.getString(R.string.settings),
            R.drawable.ic_settings,
            R.id.mapFragment
        )
        val signOut = NavigationItem(
            context.getString(R.string.sign_out),
            R.drawable.ic_arrow_left,
            null
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