package com.example.fitfactory.presentation.pages.fitnessLesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_fitness_lesson.*

class FitnessLesson : BaseFragment() {

    private val viewModel by lazy { FitnessLessonViewModel() }
    private val adapter by lazy { FitnessLessonAdapter() }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.fitness_lessons)
    override fun topBarEnabled() = true
    override fun backButtonEnabled() = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fitness_lesson, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpToLesson.isEnabled = viewModel.localStorage.isLogged()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = this@FitnessLesson.adapter
        }

        tabLayout.setupWithRecyclerView(recyclerView)

        viewModel.callResult.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
            adapter.filterData(null)
        })

        viewModel.fetchAllFitnessLesson()

    }



}
