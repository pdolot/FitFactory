package com.example.fitfactory.presentation.fragments.pass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitfactory.R
import com.example.fitfactory.data.models.Pass
import com.example.fitfactory.data.models.PassType
import com.example.fitfactory.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_pass.*

class PassFragment : BaseFragment() {

    private val adapter by lazy { PassAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pass, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topBar?.setTitle(getString(R.string.passes))
        flexibleLayout?.isViewEnable = false
        setPaddingTop(view)
        setListeners()
        setAdapter()
    }

    override fun backButtonEnabled(): Boolean = false

    private fun setAdapter() {
        passFragment_recyclerView.apply {
            this.adapter = this@PassFragment.adapter
            this.layoutManager = LinearLayoutManager(context)
        }
        adapter.setData(listOf(
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_6M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_6M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.ACCESS_1M, "20.08.2019","21.08.2019","21.08.2020",true),
            Pass(PassType.ACCESS_1M, "20.08.2019","21.08.2019","21.08.2020",false),
            Pass(PassType.PROAGE_12M, "20.08.2019","21.08.2019","21.08.2020",false)
        ))
    }

    private fun setListeners() {
        passFragment_switchPass.setOnCheckedChangeListener { _, isChecked ->
            passFragment_switchText.text = if (isChecked) getString(R.string.active) else getString(R.string.non_active)
            adapter.filterPasses(isChecked)
        }
    }
}