package com.example.fitfactory.presentation.fragments.exercises.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment

class ExerciseFragment : BaseFragment() {

    private val viewModel by lazy { ExerciseViewModel()}

    override fun backButtonEnabled(): Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flexibleLayout?.isViewEnable = false
        topBar?.setTitle("ćwiczenia")
        setPaddingTop(view)
//        body_parts.getPathModelByName("left_calf").fillColor = ContextCompat.getColor(context!!, R.color.negativeMedium)
    }
}