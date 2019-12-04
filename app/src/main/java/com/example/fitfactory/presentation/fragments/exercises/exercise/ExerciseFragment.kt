package com.example.fitfactory.presentation.fragments.exercises.exercise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_exercise.*

class ExerciseFragment : BaseFragment() {

    private val viewModel by lazy { ExerciseViewModel()}
    private val adapter by lazy { ExercisesAdapter()}



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        flexibleLayout?.isViewEnable = false
//        topBar?.setTitle("ćwiczenia")
//        setPaddingTop(view)
        adapter.setData(viewModel.getExercises())
        exercisesRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = this@ExerciseFragment.adapter
        }
        tab_layout.setupWithRecyclerView(exercisesRv)

        tab_layout.rightIconClickListener = {
            TransitionManager.beginDelayedTransition(bodyParts_content)
            bodyParts_content.visibility = if (bodyParts_content.visibility != View.GONE) View.GONE else View.VISIBLE
            tab_layout.iconColor = ContextCompat.getColor(context!!, if (bodyParts_content.visibility == View.VISIBLE) R.color.primaryLight else R.color.black65)
        }
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.exercises)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true
}