package com.example.fitfactory.presentation.pages.fitnessLesson

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.fitfactory.R

class FitnessLesson : Fragment() {

    companion object {
        fun newInstance() = FitnessLesson()
    }

    private lateinit var viewModel: FitnessLessonViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fitness_lesson, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FitnessLessonViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
