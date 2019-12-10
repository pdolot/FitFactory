package com.example.fitfactory.presentation.pages.entriesHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fitfactory.R
import com.example.fitfactory.presentation.base.BaseFragment

class EntriesHistory : BaseFragment() {

    private val viewModel by lazy { EntriesHistoryViewModel() }

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


}
