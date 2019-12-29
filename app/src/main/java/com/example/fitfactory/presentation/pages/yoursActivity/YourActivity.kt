package com.example.fitfactory.presentation.pages.yoursActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.fitfactory.R
import com.example.fitfactory.data.models.app.FitnessLesson
import com.example.fitfactory.data.models.app.LessonEntry
import com.example.fitfactory.data.models.app.StateError
import com.example.fitfactory.presentation.base.BaseFragment
import com.example.fitfactory.presentation.pages.payment.contractTerminationPayment.ContractTerminationDirections
import kotlinx.android.synthetic.main.fragment_yours_activity.*

class YourActivity : BaseFragment() {

    private val adapter by lazy { YourActivityAdapter() }
    private val viewModel by lazy { YourActivityViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yours_activity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setAdapter()

        viewModel.passesItem.passes.observe(viewLifecycleOwner, Observer {
            adapter.passesAdapter.setData(it)
        })

        viewModel.fitnessLessonItem.lessonEntries.observe(viewLifecycleOwner, Observer {
            adapter.fitnessLessonAdapter.setData(it)
        })

        viewModel.penaltyMeasureStatus.observe(viewLifecycleOwner, Observer {
            when(it){
                is StateError -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is YourActivityViewModel.SelectedPass -> {
                    viewModel.penaltyMeasureStatus.postValue(null)
                    findNavController().navigate(ContractTerminationDirections.toContractTermination(it.id, it.name, it.penalty.toFloat()))
                }
            }
        })

        viewModel.fetchLessonEntries()
        viewModel.fetchUserPasses()
    }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.yoursActivity)
    override fun topBarEnabled() = true
    override fun backButtonEnabled(): Boolean = false

    private fun setAdapter() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = this@YourActivity.adapter
        }
        tabLayout.setupWithRecyclerView(recyclerView)
        adapter.setData(listOf(viewModel.passesItem, viewModel.fitnessLessonItem))
    }

    private fun setListeners() {
        tabLayout.rightIconClickListener = {
            when(adapter.currentPosition){
                0 -> viewModel.fetchUserPasses()
                1 -> viewModel.fetchLessonEntries()
            }
        }

        adapter.fitnessLessonAdapter.onSignOutListener = {
            viewModel.signOutFromLesson(it)
        }

        adapter.passesAdapter.onContractTerminationClick = { id, name ->
            viewModel.measurePenalty(id, name)
        }
    }
}

