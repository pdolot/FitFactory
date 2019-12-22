package com.example.fitfactory.presentation.pages.fitnessLesson

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.StateComplete
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.data.models.app.StateInProgress
import com.example.fitfactory.di.Injector
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.customViews.singleChoiceDialog.SingleChoiceDialog
import kotlinx.android.synthetic.main.fragment_fitness_lesson.*
import javax.inject.Inject

class FitnessLesson : BaseFragment() {

    private val viewModel by lazy { FitnessLessonViewModel() }
    private val adapter by lazy { FitnessLessonAdapter() }

    @Inject
    lateinit var activity: AppCompatActivity

    init {
        Injector.component.inject(this)
    }

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
            adapter.filterData(adapter.filterClubId)
        })

        viewModel.signUpResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is StateInProgress -> {
                    signUpToLesson.isEnabled = false
                }
                is StateError -> {
                    signUpToLesson.isEnabled = true
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is StateComplete -> {
                    signUpToLesson.isEnabled = true
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    adapter.notifyItemChanged(adapter.currentPosition, true)
                }
            }
        })

        viewModel.fetchAllFitnessLesson()

        tabLayout.rightIconClickListener = ::filterData

        viewModel.fetchFitnessClub(viewLifecycleOwner)

        signUpToLesson.setOnClickListener {
            adapter.getFitnessLessonId()?.let {
                viewModel.signUpToLesson(it)
            }
        }
    }

    private fun filterData() {
        val dialog = SingleChoiceDialog(activity)
        dialog.setData(viewModel.fitnessClubs)
        MaterialDialog(activity).show {
            title(text = "Filtruj zajęcia fitness")
            message(text = "Wybierz fitness klub, dla którego chcesz znaleźć zajęcia fitness")
            customView(view = dialog, scrollable = true)
            positiveButton(text = "Filtruj") {
                val selectedId = dialog.getSelectedItemId()
                adapter.filterData(if (selectedId > 0) selectedId.toLong() else null)
                dismiss()
            }
        }
    }


}
