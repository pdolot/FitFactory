package com.example.fitfactory.presentation.pages.entriesHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_entries_history.*

class EntriesHistory : BaseFragment() {

    private val viewModel by lazy { EntriesHistoryViewModel() }
    private val adapter by lazy { EntriesAdapter() }

    override fun flexibleViewEnabled() = false
    override fun paddingTopEnabled() = true
    override fun topBarTitle() = getString(R.string.history)
    override fun topBarEnabled() = true
    override fun backButtonEnabled() = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entries_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchEntries()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@EntriesHistory.adapter
        }

        viewModel.entriesList.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }


}
