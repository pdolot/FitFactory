package com.example.fitfactory.presentation.pages.exercises.exercise

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_exercise.*

class ExerciseFragment : BaseFragment() {

    private val viewModel by lazy { ExerciseViewModel()}
    private val adapter by lazy { ExercisesAdapter()}
    private val args: ExerciseFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.qrCode = args.qrcode

        exercisesRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = this@ExerciseFragment.adapter
        }

        tab_layout.setupWithRecyclerView(exercisesRv)

        viewModel.exercises.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        tab_layout.rightIconClickListener = {
            TransitionManager.beginDelayedTransition(bodyParts_content)
            bodyParts_content.visibility = if (bodyParts_content.visibility != View.GONE) View.GONE else View.VISIBLE
            tab_layout.iconColor = ContextCompat.getColor(context!!, if (bodyParts_content.visibility == View.VISIBLE) R.color.primaryLight else R.color.primaryBgColor5)
        }

        tab_layout.addPositionChangeListener = {
            highlightBodyParts(adapter.getBodyPartsAtPosition(it))
        }
    }

    private fun highlightBodyParts(bodyParts: List<String>){
        val bodyPartsPaths = body_parts.getGroupModelByName("body_parts").pathModels

        bodyPartsPaths.forEach {
            if ( it.name != "outline"){
                if (bodyParts.contains(it.name.toUpperCase()) ){
                    it.fillColor = Color.parseColor("#1d8bf8")
                }else{
                    it.fillColor = Color.parseColor("#999999")
                }
            }
        }

        body_parts.update()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.exercises)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = true
}